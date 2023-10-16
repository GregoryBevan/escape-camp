# language: en

@assignContestantNextRiddleApiFeature
Feature: Assign next contestant riddle

  Scenario: Assign next contestant riddle when game is started
    Given the "Locked and Loaded" contestant registered for a game
    And the game has started
    When the contestant requests the next riddle
    Then the response contains the riddle

  Scenario: Assign next contestant riddle when contestant has already an unsolved riddle
    Given the "Locked and Loaded" contestant registered for a game
    And the game has started
    And the contestant has an assigned riddle
    When the contestant requests the next riddle
    Then the response contains a previous riddle not solved error
