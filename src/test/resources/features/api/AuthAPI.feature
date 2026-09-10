@api
Feature: Auth API

  @smoke
  Scenario: Successful login returns token
    Given I have login payload with email "eve.holt@reqres.in" and password "cityslicka"
    When I send POST request to "/login"
    Then response status code should be 200
    And response should contain "token"

  Scenario: Login without password returns 400
    Given I have login payload with email "eve.holt@reqres.in" and no password
    When I send POST request to "/login"
    Then response status code should be 400

  @smoke
  Scenario: Register with valid data returns 200
    Given I have login payload with email "eve.holt@reqres.in" and password "pistol"
    When I send POST request to "/register"
    Then response status code should be 200
    And response should contain "token"
    And response should contain "id"

  Scenario: Register without password returns 400
    Given I have login payload with email "sydney@fife.com" and no password
    When I send POST request to "/register"
    Then response status code should be 400
