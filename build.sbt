name := "ScheduleManagementSystem"

version := "1.0"

scalaVersion := "2.13.11"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-http" % "10.2.10",
  "com.typesafe.akka" %% "akka-stream" % "2.6.20",
  "com.typesafe.akka" %% "akka-http-spray-json" % "10.2.10",
  "com.typesafe.slick" %% "slick" % "3.4.1",
  "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
  "org.postgresql" % "postgresql" % "42.6.0",
  "org.quartz-scheduler" % "quartz" % "2.3.2",
  "io.spray" %% "spray-json" % "1.3.6",
  "org.scalatest" %% "scalatest" % "3.2.16" % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % "10.2.10" % Test,
  "com.h2database" % "h2" % "2.3.232" % Test,
"com.typesafe.akka" %% "akka-testkit" % "2.6.20" % Test
)
