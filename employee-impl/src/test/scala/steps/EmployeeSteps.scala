package steps

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import com.travel.cardbooking.employee.api.{ EmployeeService, ServiceMessages }
import cucumber.api.scala.{ EN, ScalaDsl }
import framework.World
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ AsyncWordSpec, Matchers }
import org.travel.cardbooking.employee.impl.EmployeeApplication

trait EmployeeSteps extends AsyncWordSpec with ScalaDsl with EN with Matchers with ScalaFutures with ServiceMessages {

  val server: ServiceTest.TestServer[EmployeeApplication with LocalServiceLocator]
  val client: EmployeeService
  val world: World

  Given("""^no employees exist$""") { () =>
    world.updateWorld(client.employeesList).map { response =>
      response.employees.size should ===(0)
    }
  }

  Given("""^no employee exists with id (.+)$""") { id: String =>
    world.updateWorld(client.getEmployee(id)).map(_ should ===(None))
  }

  Given("""^employee exists with id (.+), email (.+), name (.+), state (.+), (.+) approve$""") {
    (id: String, email: String, name: String, state: String, approver: Boolean) =>
      val requestEmp = client.Employee(id, name, email, approver, true)
      world.updateWorld(client.upsertEmployee, requestEmp).map { emp =>
        emp.id should not be empty
        emp should ===(requestEmp)
      }
  }

  When("""^I save an employee with id (.+), email (.+), name (.+), can approve$""") { (id: String, email: String, name: String) =>
    val requestEmp = client.Employee(id, name, email, true, true)
    world.updateWorld(client.upsertEmployee, requestEmp).map { emp =>
      emp.id should not be empty
      emp should ===(requestEmp)
    }
  }

  When("""^I modify the employee (.+)'s email (.+), name (.+), state (.+), (.+) approve$""") {
    (id: String, email: String, name: String, state: Boolean, approver: Boolean) =>
      val requestEmp = client.Employee(id, name, email, approver, state)
      world.updateWorld(client.upsertEmployee, requestEmp).map { emp =>
        emp.id should not be empty
        emp should ===(requestEmp)
      }
  }

  Then("""^an employee exists with id (.+), email (.+), name (.+), state active, can approve$""") { (id: String, email: String, name: String) =>

    world.checkLastHistoryRegistry { rr =>
      rr.response match {
        case Right(e: client.Employee) =>
          e.id should ===(id)
          e.email should ===(email)
          e.name should ===(name)

        case Right(r) =>
          fail(s"No error, but unexpected result. Value: Right($r)")

        case Left(t) =>
          fail(s"Error. Func: [${rr.serviceCall}] Rq: [${rr.request}] Cause: [${t.getMessage}]")
      }
    }
  }

  Then("""^an employee exists with id (.+), email (.+), name (.+), state (.+), (.+) approve$""") {
    (id: String, email: String, name: String, state: Boolean, approver: Boolean) =>
      val requestEmp = client.Employee(id, name, email, approver, state)
  }
}
