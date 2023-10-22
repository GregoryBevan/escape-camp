import {LitElement, html, css} from 'lit';
import {customElement, property, state} from 'lit/decorators.js';
import {resolveMarkdown} from 'lit-markdown';
import {styles} from './styles';

@customElement('ec-instruction')
export class EscapeCampInstruction extends LitElement {
    @property({type: String})
    instruction?: string;
    @property({type: Boolean})
    nextRiddleUnlocked: boolean = false;

    static styles = [
        styles,
        css`
        :host { display: block; background-color: var(--bg); width: 100%; }

        pre {
            background-color: black;
            padding: 16px;
            border-radius: 16px;
            color: #deeeef;
        }
        `,
    ];

    override render() {
        return html`
            <div>${resolveMarkdown(this.instruction || "")}</div>
            <button @click="${this._onNextClick}" ?disabled="${!this.nextRiddleUnlocked}">Next</button>
        `;
    }

    private _onNextClick() {
        this.dispatchEvent(new CustomEvent("nextRiddle"));
    }
}
