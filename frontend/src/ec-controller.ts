import {ReactiveController, ReactiveControllerHost} from 'lit';
import {EventSourcePolyfill} from 'event-source-polyfill';

export class EscapeCampController implements ReactiveController {
    private host: ReactiveControllerHost;
    private gameId?: string;
    private teamId?: string;
    private accessToken?: string;

    constructor(host: ReactiveControllerHost, baseUrl: string) {
        this.host = host;
        this.baseUrl = baseUrl;

        host.addController(this);
    }

    hostConnected() {

    }

    hostDisconnected() {

    }

    addTeam(teamName: string) {
        fetch(`/api/games/${this.gameId}/teams`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name: teamName}),
        })
        .then(data => data.json())
        .then(data => {
            console.log(data);
            this.teamId = data.teamId;
            this.accessToken = data.accessToken;
            this.subscribeEvents();
        });
    }

    getRiddle() {
        fetch(`/api/games/${this.gameId}/teams/${this.teamId}/riddle`, {
            headers: { "Authorization": `Bearer ${this.accessToken}` },
        })
    }

    subscribeEvents() {
        const eventSource = new EventSourcePolyfill(`/api/games/${this.gameId}/events-stream`, {
            headers: {
                "Authorization": `Bearer ${this.accessToken}`,
            }
        });

        eventSource.onopen = eventSource.onerror = eventSource.onmessage = (event) => {
            console.log(event.data);
        }
    }
}
