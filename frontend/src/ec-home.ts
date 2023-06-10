import {LitElement, html, css} from 'lit';
import {customElement, property, query} from 'lit/decorators.js';
import {choose} from 'lit/directives/choose.js';
import {styles} from './styles';

@customElement('ec-home')
export class EscapeCampHome extends LitElement {
    static styles = [
        styles,
        css`
        :host { display: block; background-color: var(--dark-blue); }
        `,
    ];

    override render() {
        return html`
            <p>Rejoindre une partie en utilisant le QR code fourni.</p>
        `;
    }

    private _onHashChange = () => {
        this.requestUpdate();
    }
}
