import com.lightbend.lagom.sbt.LagomPlugin.autoImport.lagomCassandraCleanOnStart
import sbt.Keys.testOptions
import Dependencies._

organization in ThisBuild := "org.travel"
version in ThisBuild := "1.0-SNAPSHOT"

// the Scala version that will be used for cross-compiled libraries
scalaVersion in ThisBuild := "2.12.4"

val gherkinFramework = new TestFramework("com.waioeka.sbt.runner.CucumberFramework")

lazy val `card-booking` = (project in file("."))
  .aggregate(`employee-api`, `employee-impl`, `card-booking-api`, `card-booking-impl`, `card-booking-stream-api`, `card-booking-stream-impl`)

lazy val `employee-api` = (project in file("employee-api"))
  .settings(
    libraryDependencies ++= Seq(lagomScaladslApi) ++ scalapbLibs
  )
  .settings(scalapbSettings("employee-api"))

lazy val `employee-impl` = (project in file("employee-impl"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      lagomScaladslKafkaBroker,
      lagomScaladslTestKit,
      macwire,
      scalaTest
    ) ++ cucumberLibs
  )
  .settings(
    parallelExecution in Test := false,
    CucumberPlugin.glue := "steps",
    testFrameworks += gherkinFramework,
    testOptions in Test += Tests.Argument(gherkinFramework,"--glue","classpath:steps"),
    testOptions in Test += Tests.Argument(gherkinFramework,"--plugin","html:/tmp/html"),
    testOptions in Test += Tests.Argument(gherkinFramework,"--plugin","json:/tmp/json"),
    lagomCassandraCleanOnStart in ThisBuild := true
  )
  .settings(
//    scalacOptions ++= Seq("-feature", "-language:higherKinds", "-language:implicitConversions", "-deprecation", "-Ybackend:GenBCode", "-Ydelambdafy:method", "-target:jvm-1.8")
    scalacOptions ++= Seq("-feature", "-language:higherKinds", "-language:implicitConversions", "-deprecation", "-Ydelambdafy:method", "-target:jvm-1.8")
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

/**
  * This method sets up where are the .proto files can be found for the projects and the
  * related params (like what would be the language to apply)
  *
  * Usable example is in the project:
  *   https://github.com/scalapb/ScalaPB/tree/master/examples
  * Documentation:
  *   https://trueaccord.github.io/ScalaPB/sbt-settings.html
  *
  * @param projectFolder
  * @param forJava
  * @param forServer
  * @return
  */
def scalapbSettings(projectFolder: String, forJava: Boolean = false, forServer: Boolean = false) = {

  val protoSources = PB.protoSources in Compile := Seq(file(s"$projectFolder/src/main/protobuf"))
  val pVersion = PB.protocVersion := "-v300"

  val pbgen = forJava match {
    case true =>
      PB.targets in Compile := {
        Seq(
          scalapb.gen(javaConversions = true, grpc = forServer, singleLineToProtoString = true) -> (sourceManaged in Compile).value,
          PB.gens.java("3.3.1") -> (sourceManaged in Compile).value
        )
      }
    case false =>
      PB.targets in Compile := {
        Seq( scalapb.gen(javaConversions = false, grpc = forServer, singleLineToProtoString = true) -> (sourceManaged in Compile).value )
      }
  }

  Seq(pVersion,protoSources).:+(pbgen)
}