import {LitElement, html, css} from 'lit';
import {customElement, property, query} from 'lit/decorators.js';
import {choose} from 'lit/directives/choose.js';
import {styles} from './styles';

@customElement('ec-admin')
export class EscapeCampAdmin extends LitElement {
    static styles = [
        styles,
        css`
        :host { display: block; background-color: var(--dark-blue); }
        `,
    ];

    override render() {
        return html`
            <p>Welcome master of the known universe!</p>
        `;
    }
}
