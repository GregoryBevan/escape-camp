# language: en

@addTeamApiFeature
Feature: Add team to game

  Scenario: Add first team to game
    Given a player with game identifier
    When he adds his team to the game with name "Locked and loaded"
    Then the team is added
    And a token is returned to continue the game

  Scenario: Add team with same name to game
    Given a player with game identifier
    And a team with name "Locked and loaded" has been added to the game
    When he adds his team to the game with name "Locked and loaded"
    Then the response contains a team name not available error

  Scenario: Add team to game that has already 4 teams
    Given a player with game identifier
    And 4 teams have been added to the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
      | Sher-unlock       |
    When he adds his team to the game with name "The unexpected team"
    Then the response contains a team number limit exceeded error

  Scenario: Add last team to game
    Given a player with game identifier
    And 3 teams have been added to the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
    When he adds his team to the game with name "Sher-unlock"
    Then the team is added
    And a token is returned to continue the game
    And the game is started
    And a game started notification is sent

  Scenario: Add team to an unknown game
    Given a player with an unknown game identifier
    When he adds his team to the game with name "Locked and loaded"
    Then the response contains a game not found error
