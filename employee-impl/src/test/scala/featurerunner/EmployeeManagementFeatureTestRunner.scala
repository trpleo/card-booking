package featurerunner

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  features = Array("classpath:features/Employees.feature"),
  //  tags = Array("~@Wip"),
  glue = Array("classpath:steps", "src/scala/steps"),
  plugin =
    Array(
      "pretty",
      "html:target/cucumber/html",
      "json:target/cucumber/test-report.json",
      "junit:target/cucumber/test-report.xml"))
class EmployeeManagementFeatureTestRunner
