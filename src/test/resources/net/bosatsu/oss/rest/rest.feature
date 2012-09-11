Feature: REST BDD

   Scenario: Retrieve the Commerce Resource
     When I want to interact with a resource
     Then I can GET a COMMERCE resource

   Scenario: Retrieve the Commerce Resource
     When I want to interact with a resource
     Then I can GET a COMMERCE_SLASH resource

   Scenario: Retrieve the Account Resource
     When I want to interact with a resource
     Then I am told I cannot GET a ACCOUNT resource

   Scenario: Overwrite the Account Resource
     When I want to interact with a resource
     Then I am told I cannot PUT a ACCOUNT resource

   Scenario: Overwrite the Account Resource
     When I want to interact with a resource
     Then I am told I cannot DELETE a ACCOUNT resource

   Scenario: Create an Account
     When I want to interact with a resource
     Then I can create a new Account resource