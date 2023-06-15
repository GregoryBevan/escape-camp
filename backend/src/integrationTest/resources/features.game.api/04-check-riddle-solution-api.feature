# language: en

@submitRiddleSolutionApiFeature
Feature: Submit riddle solution

  Scenario: Submit riddle solution submitted by team
    Given the "The Escape Peas" team registered for a game
    And the game has started
    And the team has an assigned riddle
    When the team submit correct solution to the riddle
    Then the riddle is solved

  Scenario: Submit riddle solution submitted by team
    Given the "Sher-unlock" team registered for a game
    And the game has started
    And the team has his last riddle assigned
    When the team submit correct solution to the riddle
    Then the riddle is solved
    And a winner announced notification is sent
