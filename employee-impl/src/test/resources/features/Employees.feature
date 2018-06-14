Feature: Adding, removing and modifying employees

  Background:
    Given no employees exist

  Scenario Outline: Adding new employee
    Given no employee exists with id <id>
#   ID should not be specified, if the intention is "create"; or must be specified, if we'd like to update an existing
#   data. Save === update in this case?
    When I save an employee with id <id>, email <email>, name <name>, can approve
    Then an employee exists with id <id>, email <email>, name <name>, state active, can approve
    Examples:
      | id  | email                  | name      |
      | SE1 | saved.emp@finastra.com | Joe Saved |

  Scenario Outline: Modifying existing employee
    Given employee exists with id <id>, email <email>, name <name>, state <state>, <is-able-to> approve
    When I modify the employee <id>'s email <new-email>, name <new-name>, state <new-state>, <new-is-able-to> approve
    Then an employee exists with id <id>, email <new-email>, name <new-name>, state <new-state>, <new-is-able-to> approve

    Examples:
      | id  | email         | new-email     | name    | new-name | state  | new-state | is-able-to | new-is-able-to |
      | ME1 | m.joe@old.com | m.joe@new.com | Joe Mod | n/a      | active | n/a       | cannot     | n/a            |
      | ME2 | m.joe@old.com | n/a           | Joe Mod | Joe New  | active | n/a       | cannot     | n/a            |
      | ME3 | m.joe@old.com | n/a           | Joe Mod | n/a      | active | inactive  | cannot     | n/a            |
      | ME4 | m.joe@old.com | n/a           | Joe Mod | n/a      | active | n/a       | cannot     | can            |
      | ME5 | m.joe@old.com | m.joe@new.com | Joe Mod | Joe New  | active | inactive  | cannot     | can            |

@currdev
  Scenario: Modifying non-existing employee
    Given no employee exists with id MN1
    When I modify the employee MN1's email dontcare@finastra.com, name No Name, state active, can approve
    Then I got an error with message No employee found with id MN1
    And there is no employee with id MN1