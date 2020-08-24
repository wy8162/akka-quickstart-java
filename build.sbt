name := "akka-quickstart-java"

version := "1.0"

scalaVersion := "2.13.1"

lazy val akkaVersion = "2.6.8"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "org.projectlombok" % "lombok" % "1.18.12",
  "junit" % "junit" % "4.12"
)
