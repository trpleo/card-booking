package steps

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import com.travel.cardbooking.employee.api.v100.{ Employee, EmployeeEnvilope }
import com.travel.cardbooking.employee.api.EmployeeService
import cucumber.api.scala.{ EN, ScalaDsl }
import framework.World
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ AsyncWordSpec, Matchers }
import org.travel.cardbooking.employee.impl.EmployeeApplication

trait EmployeeSteps extends AsyncWordSpec with ScalaDsl with EN with Matchers with ScalaFutures {

  val server: ServiceTest.TestServer[EmployeeApplication with LocalServiceLocator]
  val client: EmployeeService
  val world: World

  Given("""^no employees exist$""") { () =>
    world.updateWorld(client.employeesList).map { response =>
      response.employee.size should ===(0)
    }
  }

  Given("""^no employee exists with id (.+)$""") { id: String =>
    world.updateWorld(client.getEmployee(id)).map(_ should ===(None))
  }

  Given("""^employee exists with id (.+), email (.+), name (.+), state (.+), (.+) approve$""") {
    (id: String, email: String, name: String, state: String, approver: Boolean) =>
      val requestEmp = Employee(Some(id), Some(name), Some(email), approver, true)
      world.updateWorld(client.upsertEmployee, requestEmp).map { employeeEnvilope =>
        employeeEnvilope.error should ===(None)
        employeeEnvilope.employee.get should ===(requestEmp)
      }
  }

  When("""^I save an employee with id (.+), email (.+), name (.+), can approve$""") { (id: String, email: String, name: String) =>
    val requestEmp = Employee(Some(id), Some(name), Some(email), true, true)
    world.updateWorld(client.upsertEmployee, requestEmp).map { employeeEnvilope =>
      employeeEnvilope.error should ===(None)
      employeeEnvilope.employee.get should ===(requestEmp)
    }
  }

  When("""^I modify the employee (.+)'s email (.+), name (.+), state (.+), (.+) approve$""") {
    (id: String, email: String, name: String, state: Boolean, approver: Boolean) =>
      val requestEmp = Employee(Some(id), Some(name), Some(email), approver, state)
      world.updateWorld(client.upsertEmployee, requestEmp).map { employeeEnvilope =>
        employeeEnvilope.error should ===(None)
        employeeEnvilope.employee.get should ===(requestEmp)
      }
  }

  Then("""^an employee exists with id (.+), email (.+), name (.+), state active, can approve$""") { (id: String, email: String, name: String) =>
    world.checkHistoryRegistryWithDefaultFallback[EmployeeEnvilope] {
      case Right(ee) =>
        ee.error should ===(None)
        ee.employee.get.id should ===(Some(id))
        ee.employee.get.email should ===(Some(email))
        ee.employee.get.name should ===(Some(name))
    }
  }

  Then("""^an employee exists with id (.+), email (.+), name (.+), state (.+), (.+) approve$""") {
    (id: String, email: String, name: String, state: Boolean, approver: Boolean) =>
      val requestEmp = Employee(Some(id), Some(name), Some(email), approver, state)
      world.checkHistoryRegistryWithDefaultFallback[EmployeeEnvilope] {
        case Right(ee) =>
          ee.error should ===(None)
          ee.employee.get should ===(requestEmp)
      }
  }

  Then("""^I got an error with message (.*) with id (.*)$""") { (msg: String, id: String) =>
    world.checkHistoryRegistryWithDefaultFallback[EmployeeEnvilope] {
      case Right(ee) =>
        ee.error.isDefined should ===(true)
        ee.error.get.message should ===(msg)
        ee.error.get.code should ===(id)
        ee.error.get.employeeId should ===(id)
    }
  }

  Then("""^there is no employee with id (.*)$""") { id: String =>
    world.checkHistoryRegistryWithDefaultFallback[EmployeeEnvilope] { case Right(oe) => oe should ===(None) }
  }
}
