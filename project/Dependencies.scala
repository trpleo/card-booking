import sbt._

object Version {

  final val macwireVersion = "2.3.0"

  final val scalaTestVersion = "3.0.4"

  final val scalapbVersion = scalapb.compiler.Version.scalapbVersion
}

object Dependencies {
  import Version._

  final val macwire = "com.softwaremill.macwire" %% "macros" % macwireVersion % "provided"

  final val scalaTest = "org.scalatest" %% "scalatest" % scalaTestVersion % Test

  final val cucumberLibs = Seq(
    "io.cucumber" %% "cucumber-scala" % "2.0.1" % Test,
    "io.cucumber" % "cucumber-core"   % "2.4.0" % Test,
    "io.cucumber" % "cucumber-junit"  % "2.4.0" % Test,
    "io.cucumber" % "cucumber-java8"  % "2.4.0" % Test
  )

  final val scalapbLibs = Seq(
    "com.thesamet.scalapb" %% "scalapb-json4s" % "0.7.1",
    "com.thesamet.scalapb" %% "scalapb-runtime" % scalapbVersion % "protobuf"
  )
}