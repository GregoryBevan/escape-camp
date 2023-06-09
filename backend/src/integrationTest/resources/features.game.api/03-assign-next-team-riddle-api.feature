# language: en

@assignNextTeamRiddleApiFeature
Feature: Assign next team riddle

  Scenario: Assign next team riddle when game is started
    Given the "Locked and Loaded" team registered for a game
    And the game has started
    When the team requests the next riddle
    Then the response contains the riddle

  Scenario: Assign next team riddle when team has already an unsolved riddle
    Given the "Locked and Loaded" team registered for a game
    And the game has started
    And the team has an assigned riddle
    When the team requests the next riddle
    Then the response contains a previous riddle not solved error
