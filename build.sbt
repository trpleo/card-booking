import sbt.Keys.testOptions

organization in ThisBuild := "org.travel"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val gherkinFramework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `card-booking` = (project in file("."))
  .aggregate(`card-booking-api`, `card-booking-impl`, `card-booking-stream-api`, `card-booking-stream-impl`)

lazy val `employee-api` = (project in file("employee-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `employee-impl` = (project in file("employee-impl"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest,
      "io.cucumber" %% "cucumber-scala" % "2.0.1",
      "io.cucumber" % "cucumber-core"   % "2.4.0",
      "io.cucumber" % "cucumber-junit"  % "2.4.0",
      "io.cucumber" % "cucumber-java8"  % "2.4.0"
    )
  )
  .settings(
    parallelExecution in Test := true,
    CucumberPlugin.glue := "steps",
    testFrameworks += gherkinFramework,
    testOptions in Test += Tests.Argument(gherkinFramework,"--glue",""),
    testOptions in Test += Tests.Argument(gherkinFramework,"--plugin","html:/tmp/html"),
    testOptions in Test += Tests.Argument(gherkinFramework,"--plugin","json:/tmp/json")
  )
  .enablePlugins(CucumberPlugin)
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`employee-api`)

lazy val `card-booking-api` = (project in file("card-booking-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `card-booking-impl` = (project in file("card-booking-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`card-booking-api`)

lazy val `card-booking-stream-api` = (project in file("card-booking-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `card-booking-stream-impl` = (project in file("card-booking-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`card-booking-stream-api`, `card-booking-api`)
