package steps

import com.lightbend.lagom.scaladsl.server.LocalServiceLocator
import com.lightbend.lagom.scaladsl.testkit.ServiceTest
import com.travel.cardbooking.employee.api.EmployeeService
import cucumber.api.java.{ After, Before }
import framework.World
import org.travel.cardbooking.employee.impl.EmployeeApplication

class CucumberStepsRunner extends EmployeeSteps {

  override val server = CucumberStepsRunner.server
  override val client = CucumberStepsRunner.client
  override val world = new World

  @Before
  def beforeScenario = {
    println(s"====>>>> beforeScenario")
    //server
  }

  @After
  def afterScenario() = {
    println(s"====>>>> afterScenario")
    server.stop()
  }
}

object CucumberStepsRunner {
  lazy val server = ServiceTest.startServer(ServiceTest.defaultSetup)(new EmployeeApplication(_) with LocalServiceLocator)
  lazy val client = server.serviceClient.implement[EmployeeService]
}