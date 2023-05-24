# language: en

@createGameApiFeature
Feature: Create escape camp

  Scenario: Create an escape camp by the organizer
    Given an authenticated organizer
    When he creates an escape camp
    Then the game is created
