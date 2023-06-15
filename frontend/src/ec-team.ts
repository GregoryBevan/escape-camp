import {LitElement, html, css} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import {styles} from './styles';

@customElement('ec-team')
export class EscapeCampTeam extends LitElement {
    @property({type: String, attribute: "team-name"})
    teamName?: string;

    static styles = [
        styles,
        css`
        :host { display: block; }
        `,
    ];

    override render() {
        return html`
            <p>Bienvenue ${this.teamName} !</p>
            <p>Veuillez patienter le temps que d'autres équipes arrivent.</p>
        `;
    }
}
