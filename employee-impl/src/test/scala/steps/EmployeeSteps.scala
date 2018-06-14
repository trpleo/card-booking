package steps

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import com.travel.cardbooking.employee.api.EmployeeService
import cucumber.api.scala.{ EN, ScalaDsl }
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.{ AsyncWordSpec, Matchers }
import org.travel.cardbooking.employee.impl.EmployeeApplication

trait EmployeeSteps extends AsyncWordSpec with ScalaDsl with EN with Matchers with ScalaFutures {

  val server: ServiceTest.TestServer[EmployeeApplication with LocalServiceLocator]
  val client: EmployeeService

  Given("""^no employees exist$""") { () =>
    client.employeesList.invoke().map { response =>
      response.employees.size should ===(0)
    }
  }

  Given("""^no employee exists with id (.+)$""") { id: String =>
    client.getEmployee(id).invoke().map(_ should ===(None))
  }

  When("""^I save an employee with id (.+), email (.+), name (.+), can approve$""") { (id: String, email: String, name: String) =>

  }

  Then("""^an employee exists with id (.+), email (.+), name (.+), state active, can approve$""") { (id: String, email: String, name: String) => }

}
