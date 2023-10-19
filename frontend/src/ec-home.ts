import {LitElement, html, css} from 'lit';
import {customElement} from 'lit/decorators.js';
import {styles} from './styles';

@customElement('ec-home')
export class EscapeCampHome extends LitElement {
    static styles = [
        styles,
        css`
        :host { display: block; }
        `,
    ];

    override render() {
        return html`
            <p>Join the game using the provided QR code</p>
        `;
    }
}
