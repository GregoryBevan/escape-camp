# language: en

@gameLeaderboardApiFeature
Feature: Game leaderboard api

  Scenario: Get game leaderboard by an organizer
    Given an authenticated organizer
    And a game with enrollment type "UNLIMITED" created with identifier
    And 4 contestants have been enrolled in the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
      | Sher-unlock       |
    When he requests the game leaderboard
    Then the response contains the leaderboard
