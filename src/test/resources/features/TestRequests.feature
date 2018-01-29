Feature: Send POST and GET requests

  Scenario: Create new entity
    When User set all data in request id 1, first name Roman, last name Onyshkiv, age 30, active y and date of birth Apr 04 1987
    And Send POST request
    Then User expect to receive id 1, and status 'successfully created'

  Scenario: Get existing entity
    When User send GET request
    Then User expect to see a customer

  Scenario: Get not existing customer
    When User send GET request with not existing id
    Then User expect to receive 404 error

  Scenario: Create new entry with mandatory fields only
    When User set mandatory data in request id 2, first name Roman, last name Onyshkiv
    And Send POST request
    Then User expect to receive id 2, and status 'successfully created'

  Scenario: Get customer with mandatory field oly
    When User send GET for customer with mandatory fields only
    Then User expect to see an existing customer


