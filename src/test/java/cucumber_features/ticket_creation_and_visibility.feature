Feature: Ticket creation and visibility
  As a Service Desk Agent,
  I want to successfully create a new open ticket
  So that it appears in the open tickets list for tracking

  Background:
    Given the user is on the login page
    When the user logs with valid credentials

  Scenario: Create a ticket and verify it appears on the Open Tickets list
    Given The user is on the Open Tickets page
    When the user creates a new ticket with title Printer not working
    Then the ticket titled Printer not working should appear in the open tickets list
