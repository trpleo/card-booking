package steps

import cucumber.api.scala.{ EN, ScalaDsl }
import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures

trait EmployeeSteps extends ScalaDsl with EN with Matchers with ScalaFutures {

  Given("""^no employees exist$""") { () => }

  Given("""^no employee exists with id (.+)$""") { id: String => }

  When("""^I save an employee with id (.+), email (.+), name (.+), can approve$""") { (id: String, email: String, name: String) => }

  Then("""^an employee exists with id (.+), email (.+), name (.+), state active, can approve$""") { (id: String, email: String, name: String) => }

}
