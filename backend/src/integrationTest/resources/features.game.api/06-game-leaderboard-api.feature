# language: en

@gameLeaderboardApiFeature
Feature: Game leaderboard api

  Scenario: Get game leaderboard without enrolled contestant by an organizer
    Given an authenticated organizer
    And a game with enrollment type "UNLIMITED" created with identifier
    When he requests the game leaderboard
    Then the response contains an empty leaderboard

  Scenario: Get game leaderboard with enrolled contestants by an organizer
    Given an authenticated organizer
    And a game with enrollment type "UNLIMITED" created with identifier
    And 4 contestants have been enrolled in the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
      | Sher-unlock       |
    When he requests the game leaderboard
    Then the response contains the leaderboard in order of enrollment

  Scenario: Get game leaderboard with first solved by a contestant by an organizer
    Given an authenticated organizer
    And a game with enrollment type "UNLIMITED" created with identifier
    And 4 contestants have been enrolled in the game
      | Locked and Loaded |
      | Jeepers Keypers   |
      | The Escape Peas   |
      | Sher-unlock       |
    And the first riddle has been unlocked
    And the "Jeepers Keypers" submit correct solution to the riddle 1
    When he requests the game leaderboard
    Then the response contains the leaderboard in order of resolution
