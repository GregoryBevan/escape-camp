# language: en

@submitRiddleSolutionApiFeature
Feature: Submit riddle solution

  Scenario: Submit riddle solution submitted by contestant
    Given the "The Escape Peas" contestant registered for a game
    And the game has started
    And the contestant has an assigned riddle
    When the contestant submit correct solution to the riddle
    Then the riddle is solved

  Scenario: Submit riddle solution submitted by contestant
    Given the "Sher-unlock" contestant registered for a game
    And the game has started
    And the contestant has his last riddle assigned
    When the contestant submit correct solution to the riddle
    Then the riddle is solved
    And a winner announced notification is sent
