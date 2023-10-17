import {ReactiveController} from 'lit';
import {EventSourcePolyfill} from 'event-source-polyfill';
import {EscapeCampApp} from './ec-app';

import instruction0 from './instructions/0.md?raw';
import instruction1 from './instructions/1.md?raw';
import instruction2 from './instructions/2.md?raw';
import instruction3 from './instructions/3.md?raw';
import instruction4 from './instructions/4.md?raw';

const instructionTexts = [
    instruction0, instruction1, instruction2, instruction3, instruction4
];

export class EscapeCampController implements ReactiveController {
    private host: EscapeCampApp;
    private _gameId?: string;
    contestantId?: string;
    contestantName?: string;
    riddleId?: string;
    riddleText?: string;
    nextRiddleAvailable: boolean = false;
    instructionText: string = instructionTexts[0];
    solvedRiddleCount: number = 0;
    private accessToken?: string;
    private eventSource?: EventSourcePolyfill;

    public get gameId() {
        return this._gameId;
    }

    public set gameId(gameId: string | undefined) {
        if (gameId != this.gameId) {
            this._gameId = gameId;
            this.contestantId = undefined;
            this.contestantName = undefined;
            this.accessToken = undefined;
            this.riddleId = undefined;
            this.riddleText = undefined;
            this.nextRiddleAvailable = false;
            this.instructionText = instructionTexts[0];
            this.solvedRiddleCount = 0;
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
            this.contestantId = item.contestantId;
            this.contestantName = item.contestantName;
            this.riddleId = item.riddleId;
            this.riddleText = item.riddleText;
            this.nextRiddleAvailable = item.nextRiddleAvailable;
            this.instructionText = item.instructionText;
            this.solvedRiddleCount = item.solvedRiddleCount;
            this.accessToken = item.accessToken;
        }

        if (this.gameId && this.contestantId && this.accessToken) {
            this.subscribeEvents();
        }
    }

    hostDisconnected() {
        this.unsubscribeEvents();
    }

    async addContestant(contestantName: string): Promise<string> {
        const response = await fetch(`/api/games/${this.gameId}/contestants`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({name: contestantName}),
        })
        const data = await response.json();

        console.log(data);
        this.contestantName = contestantName;
        this.contestantId = data.contestantId;
        this.accessToken = data.accessToken;

        this.persistData();
        this.subscribeEvents();

        return data.eventType;
    }

    async getRiddle(): Promise<string> {
        const response = await fetch(`/api/games/${this.gameId}/contestants/${this.contestantId}/riddle`, {
            headers: {"Authorization": `Bearer ${this.accessToken}`},
        });
        const riddle = await response.json();
        this.riddleId = riddle.riddle.name;
        this.riddleText = riddle.riddle.content;
        this.instructionText = instructionTexts[this.solvedRiddleCount];
        this.nextRiddleAvailable = false;
        this.persistData();
        return riddle.riddle.content;
    }

    async guess(solution: string): Promise<boolean> {
        const response = await fetch(`/api/games/${this.gameId}/contestants/${this.contestantId}/riddle/${this.riddleId}`, {
            method: "POST",
            headers: {
                "Authorization": `Bearer ${this.accessToken}`,
                "Content-Type": "application/json",
            },
            body: JSON.stringify({solution}),
        });
        const result = response.status == 200;
        if (result) {
            this.solvedRiddleCount += 1;
            this.riddleId = undefined;
            this.riddleText = undefined;
            this.persistData();
        }

        return result;
    }

    subscribeEvents() {
        if (!("Notification" in window)) {
            alert("This browser does not support notifications");
        } else if (Notification.permission === "default") {
            Notification.requestPermission();
        }
        this.eventSource = new EventSourcePolyfill(`/api/games/${this.gameId}/events-stream`, {
            headers: {
                "Authorization": `Bearer ${this.accessToken}`,
            }
        });

        this.eventSource.addEventListener("ContestantEnrolled", event => {
            console.log(event.data);
        });
        this.eventSource.addEventListener("NextRiddleUnlocked", event => {
            this._onNextRiddle(event);
        });
        this.eventSource.addEventListener("RiddleSolved", async event => {
            await this.notifyRiddleSolved(event.data);
        });
        this.eventSource.addEventListener("WinnerAnnounced", async event => {
            await this.notifyWinnerAnnounced(event.data);
        });
    }

    unsubscribeEvents() {
        this.eventSource?.close();
    }

    private persistData() {
        const data = {
            gameId: this.gameId,
            contestantName: this.contestantName,
            contestantId: this.contestantId,
            riddleId: this.riddleId,
            riddleText: this.riddleText,
            nextRiddleAvailable: this.nextRiddleAvailable,
            instructionText: this.instructionText,
            solvedRiddleCount: this.solvedRiddleCount,
            accessToken: this.accessToken,
        };
        window.localStorage.setItem("controller", JSON.stringify(data));
    }

    private _onNextRiddle(event: Event) {
        this.nextRiddleAvailable = true;
    }

    async notifyRiddleSolved(data: any) {
        if (Notification.permission === "granted") {
            const registration = await navigator.serviceWorker.ready;
            await registration.showNotification("Escape Camp", {body: "Une personne a résolu une énigme"});

        }
    }

    async notifyWinnerAnnounced(data: any) {
        if (Notification.permission === "granted") {
            const registration = await navigator.serviceWorker.ready;
            await registration.showNotification("Escape Camp", {body: "Une personne a gagné"});

        }
    }
}
