import {LitElement, html, css} from 'lit';
import {customElement, property, state} from 'lit/decorators.js';
import {resolveMarkdown} from 'lit-markdown';
import {styles} from './styles';

@customElement('ec-riddle')
export class EscapeCampRiddle extends LitElement {
    @property({type: String})
    riddle?: string;

    @state()
    private _guess: string = "";

    static styles = [
        styles,
        css`
        :host { display: block; background-color: #183c5c; }
        `,
    ];

    override render() {
        return html`
            <p>${resolveMarkdown(this.riddle || "")}</p>
            <input id="guess" type="text" @input="${this._onInput}" />
            <button @click="${this._onGoClick}" ?disabled="${!this.isValid()}">GO</button>
        `;
    }

    private isValid() {
        return Boolean(this._guess);
    }

    private _onInput(e: Event) {
        this._guess = (e.target as HTMLInputElement).value.trim();
    }

    private _onGoClick() {
        const guess = this._guess;

        this.dispatchEvent(new CustomEvent("guess", { detail: { guess } }));
    }
}
