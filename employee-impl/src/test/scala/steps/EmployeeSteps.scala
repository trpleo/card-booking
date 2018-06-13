package steps

import cucumber.api.scala.{ EN, ScalaDsl }
import org.scalatest.Matchers
import org.scalatest.concurrent.ScalaFutures

trait EmployeeSteps extends ScalaDsl with EN with Matchers with ScalaFutures {

  Given("""^no employee exists with id (.+)$""") { id: String => }

}
