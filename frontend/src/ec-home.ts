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
            <p>Rejoindre une partie en utilisant le QR code fourni.</p>
        `;
    }
}
