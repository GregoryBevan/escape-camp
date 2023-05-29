# language: en

@addTeamApiFeature
Feature: Add team to game

  Scenario: Add first team to game
    Given a player with game identifier
    When he adds his team to the game with name "Locked and loaded"
    Then the team is added
    And a token is returned to continue the game
