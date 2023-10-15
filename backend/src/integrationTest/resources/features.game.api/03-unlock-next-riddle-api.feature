# language: en

@unlockNextRiddleApiFeature
Feature: Unlock the game's next riddle

  Scenario: Unlock the first riddle of the game with unlimited contestants
    Given an authenticated organizer
    And a game with enrollment type "UNLIMITED" created with identifier
    And 4 contestants have been enrolled in the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
      | Sher-unlock       |
    When he unlocks the next riddle
    Then the riddle is unlocked for all contestants

  Scenario: Unlock the first riddle of the game with limited contestants
    Given an authenticated organizer
    And a game with enrollment type "LIMITED_TO_RIDDLE_NUMBER" created with identifier
    And 3 contestants have been enrolled in the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
    When he unlocks the next riddle
    Then the response contains a next riddle unlock not allowed error

  Scenario: Unlock the first riddle of an unknown game
    Given an authenticated organizer
    And an unknown game identifier
    When he unlocks the next riddle
    Then the response contains a game not found error
