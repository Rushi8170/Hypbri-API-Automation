@api
Feature: User API

  @smoke
  Scenario: Get all users returns 200
    When I send GET request to "/users"
    Then response status code should be 200
    And response should contain "data"

  @smoke
  Scenario: Get user by id returns 200
    When I send GET request to "/users/2"
    Then response status code should be 200
    And response field "data.id" should be "2"

  Scenario: Get non-existing user returns 404
    When I send GET request to "/users/9999"
    Then response status code should be 404

  @smoke
  Scenario: Create a new user returns 201
    Given I have user payload with name "John" and job "QA Engineer"
    When I send POST request to "/users"
    Then response status code should be 201
    And response should contain "id"

  Scenario: Update a user returns 200
    Given I have user payload with name "Jane" and job "Senior QA"
    When I send PUT request to "/users/2"
    Then response status code should be 200
    And response should contain "updatedAt"

  Scenario: Delete a user returns 204
    When I send DELETE request to "/users/2"
    Then response status code should be 204
