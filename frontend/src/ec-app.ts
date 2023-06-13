import {LitElement, html, css, unsafeCSS} from 'lit';
import {customElement} from 'lit/decorators.js';
import {choose} from 'lit/directives/choose.js';
import {styles} from './styles';
import './ec-home';
import './ec-admin';
import './ec-game';
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

    override async connectedCallback() {
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
        let m: RegExpMatchArray|null = null;
        let riddle = "";
        if (m = hash.match(/^#admin$/)) {
            page = "admin";
        } else if (m = hash.match(/^#game\/([^/]+)$/)) {
            page = "game";
        } else if (m = hash.match(/^#riddle$/)) {
            page = "riddle";
            riddle = `
# First riddle

Hello, *world*
            `;
        }

        return html`
            <h1>Escape Camp</h1>
            <main>
            ${choose(page, [
                ["home", () => html`<ec-home></ec-home>`],
                ["admin", () => html`<ec-admin></ec-admin>`],
                ["game", () => html`<ec-game game-id="${m?.[1]}" @addTeam="${this._onAddTeam}"></ec-game>`],
                ["riddle", () => html`<ec-riddle .riddle="${riddle}" @guess="${this._onGuess}"></ec-riddle>`],
            ])}
            </main>
        `;
    }

    private _onHashChange = () => {
        this.requestUpdate();
    }

    private _onAddTeam(e: CustomEvent) {
        console.log(e.detail.teamName);
    }

    private _onGuess(e: CustomEvent) {
        console.log(e.detail.guess);
    }
}
