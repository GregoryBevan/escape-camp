import {LitElement, html, css} from 'lit';
import {customElement} from 'lit/decorators.js';
import {styles} from './styles';

@customElement('ec-admin')
export class EscapeCampAdmin extends LitElement {
    static styles = [
        styles,
        css`
        :host { display: block; background-color: #183c5c; }
        `,
    ];

    override render() {
        return html`
            <p>Welcome master of the known universe!</p>
        `;
    }
}
