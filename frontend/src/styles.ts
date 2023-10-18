import {css} from 'lit';

export const styles = css`
    * {
        box-sizing: border-box;
    }
    input, select {
        padding: 24px;
        border: 1px solid #2e72ac;
        border-radius: 24px;
        background-color: #99c8ce;
        color: #2e72ac;
        font-family: inherit;
        font-size: inherit;
        font-weight: inherit;
        text-decoration: inherit;
    }
    button, .button {
        padding: 24px;
        border: 1px solid #2e72ac;
        border-radius: 24px;
        background-color: #2e72ac;
        color: #99c8ce;
        font-family: inherit;
        font-size: inherit;
        font-weight: inherit;
        text-decoration: inherit;
    }
    select, button, .button {
        box-shadow: 2px 8px 8px rgba(17, 1, 1, 0.2);
    }
    button:active {
        box-shadow: none;
    }
    button:disabled {
        border-color: #bcdbdf;
        background-color: #bcdbdf;
        box-shadow: none;
        color: #99c8ce;
    }
    .wide {
        padding: 4px 16px;
    }
`;
