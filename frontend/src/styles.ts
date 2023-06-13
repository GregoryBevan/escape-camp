import {css} from 'lit';

export const styles = css`
    * {
        box-sizing: border-box;
    }
    input, select, button, .button {
        padding: 4px;
        border: 1px solid brown;
        border-radius: 4px;
        background-color: brown;
        color: inherit;
        font-family: inherit;
        font-size: inherit;
        font-weight: inherit;
        text-decoration: inherit;
    }
    select, button, .button {
        box-shadow: 2px 4px 4px rgba(17, 1, 1, 0.2);
    }
    button:active {
        box-shadow: none;
    }
    button:disabled {
        border-color: gray;
        background-color: gray;
        box-shadow: none;
    }
    .wide {
        padding: 4px 16px;
    }
`;
