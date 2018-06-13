import sbt.util

logLevel := util.Level.Info

// The Lagom plugin
addSbtPlugin("com.lightbend.lagom" % "lagom-sbt-plugin" % "1.4.6")
// Needed for importing the project into Eclipse
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.4")

addSbtPlugin("com.waioeka.sbt" % "cucumber-plugin" % "0.1.7")

addSbtPlugin("org.scalariform" % "sbt-scalariform" % "1.8.2")
