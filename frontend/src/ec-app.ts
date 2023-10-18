import {LitElement, html, css, unsafeCSS} from 'lit';
import {customElement, state} from 'lit/decorators.js';
import {choose} from 'lit/directives/choose.js';
import {until} from 'lit/directives/until.js';
import {Task} from '@lit-labs/task';
import {styles} from './styles';
import {EscapeCampController} from './ec-controller';
import './ec-home';
import './ec-admin';
import './ec-game';
import './ec-contestant';
import './ec-riddle';
import './ec-instruction';
import logo from './escape.svg?url';

@customElement('ec-app')
export class EscapeCampApp extends LitElement {
    static styles = [
        styles,
        css`
        :host { display: block; background-color: var(--bg); min-height: 100%; }
        h1 {
            width: 100%; height: 100px; padding-left: 200px; margin: 0px;
            color: darkgray;
            font-family: "VT323"; font-weight: inherit;
            background: url("${unsafeCSS(logo)}") bottom left/200px no-repeat, cyan;
        }
        main { width: 100%; padding: 0 48px 48px; }
        `,
    ];

    private controller = new EscapeCampController(this);

    override connectedCallback() {
        super.connectedCallback();
        window.addEventListener("hashchange", this._onHashChange);
    }

    override disconnectedCallback() {
        window.removeEventListener("hashchange", this._onHashChange);
        super.disconnectedCallback();
    }

    override render() {
        const hash = window.location.hash;
        let page = "home";
        let m: RegExpMatchArray | null = null;
        let riddle;
        if (m = hash.match(/^#admin$/)) {
            page = "admin";
        } else if (m = hash.match(/^#game\/([^/]+)$/)) {
            page = "game";
            this.controller.gameId = m[1];
        } else if (m = hash.match(/^#game\/([^/]+)\/contestant\/([^/]+)$/)) {
            page = "contestant";
        } else if (m = hash.match(/^#riddle$/)) {
            page = "riddle";
            riddle = this.controller.getRiddle().then(riddleText => html`<ec-riddle .riddle="${riddleText}" @guess="${this._onGuess}"></ec-riddle>`);
        } else if (m = hash.match(/^#instruction$/)) {
            page = "instruction";
        } else if (m = hash.match(/^#good$/)) {
            page = "good";
        } else if (m = hash.match(/^#try-again$/)) {
            page = "try-again";
        } else if (m = hash.match(/^#finished$/)) {
            page = "finished";
        }

        return html`
            <h1>Escape Camp</h1>
            <main>
            ${choose(page, [
                ["home", () => html`<ec-home></ec-home>`],
                ["admin", () => html`<ec-admin></ec-admin>`],
                ["game", () => html`<ec-game game-id="${this.controller.gameId}" @addContestant="${this._onAddContestant}"></ec-game>`],
                ["instruction", () => html`<ec-instruction .instruction="${this.controller.instructionText}" .nextRiddleUnlocked="${this.controller.nextRiddleUnlocked}" @nextRiddle="${this.onNextRiddle}"></ec-instruction>`],
                ["good", () => html`<p>Bonne réponse !</p><a class="button" href="#instruction">Suivant</a>`],
                ["try-again", () => html`<p>Essayez encore !</p><a class="button" href="#riddle">OK</a>`],
                ["riddle", () => until(riddle, html`<p>...</p>`)],
                ["finished", () => html`<p>Bravo ! Vous avez terminé le jeu</p><p>Maintenant que vous avez les quatre mots-clés, vous pouvez deviner le sujet de notre conférence.</p>`],
            ])}
            </main>
        `;
    }

    private _onHashChange = () => {
        this.requestUpdate();
    }

    private async _onAddContestant(e: CustomEvent) {
        const eventType = await this.controller.addContestant(e.detail.contestantName);
        window.location.hash = `#instruction`;
    }

    onNextRiddle() {
        window.location.hash = "#riddle";
    }

    private async _onGuess(e: CustomEvent) {
        if (await this.controller.guess(e.detail.guess)) {
            if (this.controller.solvedRiddleCount >= 4) {
                window.location.hash = "#finished";
            } else {
                window.location.hash = "#good";
            }
        } else {
            window.location.hash = "#try-again";
        }
    }
}
