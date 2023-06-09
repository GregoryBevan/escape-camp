# language: en

@submitRiddleSolutionApiFeature
Feature: Submit riddle solution

  Scenario: Submit riddle solution submitted by team
    Given the "The Escape Peas" team registered for a game
    And the game has started
    And the team has an assigned riddle
    When the team submit correct solution to the riddle
    Then the riddle is solved
