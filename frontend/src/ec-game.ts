import {LitElement, html, css} from 'lit';
import {customElement, property, state} from 'lit/decorators.js';
import {styles} from './styles';

@customElement('ec-game')
export class EscapeCampGame extends LitElement {
    @property({type: String, attribute: "game-id"})
    gameId?: string;

    @state()
    private _contestantName: string = "";

    static styles = [
        styles,
        css`
        :host { display: block; }
        `,
    ];

    override render() {
        return html`
            <p>Quel est ton pseudo ?</p>
            <input id="contestantName" type="text" @input="${this._onInput}" />
            <button @click="${this._onGoClick}" ?disabled="${!this.isValid()}">GO</button>
        `;
    }

    private isValid() {
        return Boolean(this._contestantName);
    }

    private _onInput(e: Event) {
        this._contestantName = (e.target as HTMLInputElement).value.trim();
    }

    private _onGoClick() {
        const contestantName = this._contestantName;

        this.dispatchEvent(new CustomEvent("addContestant", { detail: { contestantName } }));
    }
}
