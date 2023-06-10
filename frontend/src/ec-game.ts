import {LitElement, html, css} from 'lit';
import {customElement, property, state, query} from 'lit/decorators.js';
import {choose} from 'lit/directives/choose.js';
import {styles} from './styles';

@customElement('ec-game')
export class EscapeCampGame extends LitElement {
    @property({type: String, attribute: "game-id"})
    gameId?: string;

    @state()
    private _teamName: string = "";

    static styles = [
        styles,
        css`
        :host { display: block; background-color: var(--dark-blue); }
        `,
    ];

    override render() {
        return html`
            <p>Comment s'appelle votre équipe ?</p>
            <input id="teamName" type="text" @input="${this._onInput}" />
            <button @click="${this._onGoClick}" ?disabled="${!this.isValid()}">GO</button>
        `;
    }

    private isValid() {
        return Boolean(this._teamName);
    }

    private _onInput(e: Event) {
        this._teamName = (e.target as HTMLInputElement).value.trim();
    }

    private _onGoClick() {
        const teamName = this._teamName;

        this.dispatchEvent(new CustomEvent("addTeam", { detail: { teamName } }));
    }
}
