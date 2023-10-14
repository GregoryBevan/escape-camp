# language: en

@createGameApiFeature
Feature: Create escape camp

  Scenario: Create an escape camp by the organizer
    Given an authenticated organizer
    When he creates an escape camp
    Then the game is created

  Scenario Outline: Create an escape camp with specified enrollment type by the organizer
    Given an authenticated organizer
    When he creates an escape camp with enrollment type "<Enrollment type>"
    Then the game is created
    Examples:
      | Enrollment type          |
      | LIMITED_TO_RIDDLE_NUMBER |
      | UNLIMITED                |

  Scenario: Create an escape camp by an unauthenticated user
    Given an unauthenticated user
    When he creates an escape camp
    Then an authentication error is returned
