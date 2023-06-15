import {LitElement, html, css, unsafeCSS} from 'lit';
import {customElement, state} from 'lit/decorators.js';
import {choose} from 'lit/directives/choose.js';
import {Task} from '@lit-labs/task';
import {styles} from './styles';
import {EscapeCampController} from './ec-controller';
import './ec-home';
import './ec-admin';
import './ec-game';
import './ec-team';
import './ec-riddle';
import logo from './escape.svg?url'

@customElement('ec-app')
export class EscapeCampApp extends LitElement {
    static styles = [
        styles,
        css`
        :host { display: block; background-color: var(--bg); }
        h1 {
            width: 100%; height: 100px; padding-left: 200px;
            color: darkgray;
            font-family: "VT323"; font-weight: inherit;
            background: url("${unsafeCSS(logo)}") bottom left/200px no-repeat, cyan;
        }
        main { width: 100%; height: calc(100% - 100px); padding: 0 48px; }
        `,
    ];

    @state()
    private riddleText: string = "...";

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
        let riddle = "";
        if (m = hash.match(/^#admin$/)) {
            page = "admin";
        } else if (m = hash.match(/^#game\/([^/]+)$/)) {
            page = "game";
            this.controller.gameId = m[1];
        } else if (m = hash.match(/^#game\/([^/]+)\/team\/([^/]+)$/)) {
            page = "team";
        } else if (m = hash.match(/^#riddle$/)) {
            page = "riddle";
            this.controller.getRiddle().then(riddleText => { this.riddleText = riddleText; this.requestUpdate(); });
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
                ["team", () => html`<ec-team team-name="${this.controller.teamName}"></ec-team>`],
                ["admin", () => html`<ec-admin></ec-admin>`],
                ["game", () => html`<ec-game game-id="${this.controller.gameId}" @addTeam="${this._onAddTeam}"></ec-game>`],
                ["good", () => html`<p>Bonne réponse !</p><a class="button" href="#riddle">Suivant</a>`],
                ["try-again", () => html`<p>Essayez encore !</p><a class="button" href="#riddle">OK</a>`],
                ["riddle", () => html`<ec-riddle .riddle="${this.riddleText}" @guess="${this._onGuess}"></ec-riddle>`],
                ["finished", () => html`<p>Bravo ! Vous avez terminé le jeu</p><p>Maintenant que vous avez les quatre mots-clés, vous pouvez deviner le sujet de notre conférence.</p>`],
            ])}
            </main>
        `;
    }

    private _onHashChange = () => {
        this.requestUpdate();
    }

    private async _onAddTeam(e: CustomEvent) {
        const eventType = await this.controller.addTeam(e.detail.teamName);
        if (eventType == "TeamAdded") {
            window.location.hash = `#game/${this.controller.gameId}/team/${this.controller.teamId}`;
        } else if (eventType == "GameStarted") {
            window.location.hash = "#riddle";
        }
    }

    onGameStarted() {
        window.location.hash = "#riddle";
    }

    private async _onGuess(e: CustomEvent) {
        if (await this.controller.guess(e.detail.guess)) {
            window.location.hash = "#good";
        } else {
            window.location.hash = "#try-again";
        }
    }
}
