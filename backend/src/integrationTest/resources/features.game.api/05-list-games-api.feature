# language: en

@listGamesApiFeature
Feature: List games api

  Scenario: List games api by organizer
    Given an authenticated organizer
    When he requests the game list
    Then the response contains all games
