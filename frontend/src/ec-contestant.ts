import {LitElement, html, css} from 'lit';
import {customElement, property} from 'lit/decorators.js';
import {styles} from './styles';

@customElement('ec-contestant')
export class EscapeCampContestant extends LitElement {
    @property({type: String, attribute: "contestant-name"})
    contestantName?: string;

    static styles = [
        styles,
        css`
        :host { display: block; }
        `,
    ];

    override render() {
        return html`
            <p>Bienvenue ${this.contestantName} !</p>
            <p>Veuillez patienter le temps que d'autres participant⋅es arrivent.</p>
        `;
    }
}
