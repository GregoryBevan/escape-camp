# language: en

@enrollContestantApiFeature
Feature: Enroll contestant to game

  Scenario Outline: Enroll first contestant to game
    Given a contestant with identifier of game with enrollment type "<Enrollment type>"
    When he enrolls in the game with name "Locked and loaded"
    Then the contestant is enrolled
    And a token is returned to continue the game
    Examples:
      | Enrollment type          |
      | LIMITED_TO_RIDDLE_NUMBER |
      | UNLIMITED                |

  Scenario Outline: Enroll contestant with same name to game
    Given a contestant with identifier of game with enrollment type "<Enrollment type>"
    And a contestant with name "Locked and loaded" has been enrolled to the game
    When he enrolls in the game with name "Locked and loaded"
    Then the response contains a contestant name not available error
    Examples:
      | Enrollment type          |
      | LIMITED_TO_RIDDLE_NUMBER |
      | UNLIMITED                |

  Scenario: Enroll contestant to game with contestant limit that has already 4 contestants
    Given a contestant with identifier of game with enrollment type "LIMITED_TO_RIDDLE_NUMBER"
    And 4 contestants have been enrolled in the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
      | Sher-unlock       |
    When he enrolls in the game with name "The unexpected contestant"
    Then the response contains a contestant number limit exceeded error

  Scenario: Enroll contestant to game without contestant limit that has already 4 contestants
    Given a contestant with identifier of game with enrollment type "UNLIMITED"
    And 4 contestants have been enrolled in the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
      | Sher-unlock       |
    When he enrolls in the game with name "The unexpected contestant"
    Then the contestant is enrolled
    And a token is returned to continue the game

  Scenario: Enroll last contestant to game with contestant limit
    Given a contestant with identifier of game with enrollment type "LIMITED_TO_RIDDLE_NUMBER"
    And 3 contestants have been enrolled in the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
    When he enrolls in the game with name "Sher-unlock"
    Then the contestant is enrolled
    And a token is returned to continue the game
    And the game starts automatically
    And a game started notification is sent

  Scenario: Enroll contestant to an unknown game
    Given a contestant with an unknown game identifier
    When he enrolls in the game with name "Locked and loaded"
    Then the response contains a game not found error
