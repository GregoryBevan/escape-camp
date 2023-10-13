# language: en

@addContestantApiFeature
Feature: Add contestant to game

  Scenario: Add first contestant to game
    Given a contestant player with game identifier
    When he adds his contestant to the game with name "Locked and loaded"
    Then the contestant is added
    And a token is returned to continue the game

  Scenario: Add contestant with same name to game
    Given a contestant player with game identifier
    And a contestant with name "Locked and loaded" has been added to the game
    When he adds his contestant to the game with name "Locked and loaded"
    Then the response contains a contestant name not available error

  Scenario: Add contestant to game that has already 4 contestants
    Given a contestant player with game identifier
    And 4 contestants have been added to the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
      | Sher-unlock       |
    When he adds his contestant to the game with name "The unexpected contestant"
    Then the response contains a contestant number limit exceeded error

  Scenario: Add last contestant to game
    Given a contestant player with game identifier
    And 3 contestants have been added to the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
    When he adds his contestant to the game with name "Sher-unlock"
    Then the contestant is added
    And a token is returned to continue the game
    And the game starts automatically
    And a game started notification is sent

  Scenario: Add contestant to an unknown game
    Given a contestant player with an unknown game identifier
    When he adds his contestant to the game with name "Locked and loaded"
    Then the response contains a game not found error
