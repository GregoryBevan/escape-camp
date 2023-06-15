import {ReactiveController} from 'lit';
import {EventSourcePolyfill} from 'event-source-polyfill';
import {EscapeCampApp} from './ec-app';

export class EscapeCampController implements ReactiveController {
    private host: EscapeCampApp;
    private _gameId?: string;
    teamId?: string;
    teamName?: string;
    riddleId?: string;
    private accessToken?: string;
    private eventSource?: EventSourcePolyfill;

    public get gameId() {
        return this._gameId;
    }

    public set gameId(gameId: string | undefined) {
        if (gameId != this.gameId) {
            this._gameId = gameId;
            this.teamId = undefined;
            this.teamName = undefined;
            this.accessToken = undefined;
            this.unsubscribeEvents();
        }
    }

    constructor(host: EscapeCampApp) {
        this.host = host;

        host.addController(this);
    }

    hostConnected() {
        console.log("Connected");
        const item = JSON.parse(window.localStorage.getItem("controller") || "{}");

        if (item) {
            this.gameId = item.gameId;
            this.teamId = item.teamId;
            this.teamName = item.teamName;
            this.riddleId = item.riddleId;
            this.accessToken = item.accessToken;
        }

        if (this.gameId && this.teamId && this.accessToken) {
            this.subscribeEvents();
        }
    }

    hostDisconnected() {
        this.unsubscribeEvents();
    }

    async addTeam(teamName: string): Promise<string> {
        const response = await fetch(`/api/games/${this.gameId}/teams`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name: teamName}),
        })
        const data = await response.json();

        console.log(data);
        this.teamName = teamName;
        this.teamId = data.teamId;
        this.accessToken = data.accessToken;

        this.persistData();
        this.subscribeEvents();

        return data.eventType;
    }

    async getRiddle(): Promise<string> {
        const response = await fetch(`/api/games/${this.gameId}/teams/${this.teamId}/riddle`, {
            headers: { "Authorization": `Bearer ${this.accessToken}` },
        });
        const riddle = await response.json();
        this.riddleId = riddle.riddle.name;
        return riddle.riddle.content;
    }

    async guess(solution: string): Promise<boolean> {
        const response = await fetch(`/api/games/${this.gameId}/teams/${this.teamId}/riddle/${this.riddleId}`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${this.accessToken}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({solution}),
        });

        return response.status == 200
    }

    subscribeEvents() {
        console.log("Subscribed");
        this.eventSource = new EventSourcePolyfill(`/api/games/${this.gameId}/events-stream`, {
            headers: {
                "Authorization": `Bearer ${this.accessToken}`,
            }
        });

        this.eventSource.addEventListener("TeamAdded", event => {
            console.log(event.data);
        });
        this.eventSource.addEventListener("GameStarted", event => {
            this.host.onGameStarted();
        });
    }

    unsubscribeEvents() {
        this.eventSource?.close();
    }

    private persistData() {
        const data = {
            gameId: this.gameId,
            teamName: this.teamName,
            teamId: this.teamId,
            riddleId: this.riddleId,
            accessToken: this.accessToken
        };
        window.localStorage.setItem("controller", JSON.stringify(data));
    }
}
