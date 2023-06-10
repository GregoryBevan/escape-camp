import {LitElement, html, css} from 'lit';
import {customElement, property, query} from 'lit/decorators.js';
import {choose} from 'lit/directives/choose.js';
import {styles} from './styles';
import './ec-home';
import './ec-admin';
import './ec-game';
import './ec-riddle';

@customElement('ec-app')
export class EscapeCampApp extends LitElement {
    static styles = [
        styles,
        css`
        :host { display: block; background-color: var(--dark-blue); padding: 16px; }
        h1 { width: 100%; height: 48px; }
        .screen { width: 100%; height: calc(100% - 48px); }
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
        let m = null;
        if (m = hash.match(/^#admin$/)) {
            page = "admin";
        } else if (m = hash.match(/^#game\/([^/]+)$/)) {
            page = "game";
        } else if (m = hash.match(/^#riddle\/([^/]+)$/)) {
            page = "riddle";
        }

        return html`
            <h1>Escape Camp</h1>
            ${choose(page, [
                ["home", () => html`<ec-home class="screen"></ec-home>`],
                ["admin", () => html`<ec-admin class="screen"></ec-admin>`],
                ["game", () => html`<ec-game class="screen" game-id="${m[1]}" @addTeam="${this._onAddTeam}"></ec-game>`],
                ["riddle", () => html`<ec-riddle class="screen" .riddle="Hello, riddle ${m[1]}"></ec-riddle>`],
            ])}
        `;
    }

    private _onHashChange = () => {
        this.requestUpdate();
    }

    private _onAddTeam(e: CustomEvent) {
        console.log(e.detail.teamName);
    }
}
