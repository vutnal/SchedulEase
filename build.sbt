ThisBuild / scalaVersion := "2.13.12"

lazy val root = (project in file("."))
  .settings(
    name := "ScheduleEase",
    version := "1.0",
    libraryDependencies ++= Seq(
      // Akka HTTP
      "com.typesafe.akka" %% "akka-http" % "10.2.10",
      "com.typesafe.akka" %% "akka-stream" % "2.6.20",
      // Slick
      "com.typesafe.slick" %% "slick" % "3.4.1",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.4.1",
      // PostgreSQL Driver
      "org.postgresql" % "postgresql" % "42.6.0",
      // JSON Serialization
      "de.heikoseeberger" %% "akka-http-circe" % "1.40.0-RC3",
      "io.circe" %% "circe-generic" % "0.15.0-M1",
      // Logging
      "ch.qos.logback" % "logback-classic" % "1.4.7",
      "org.scalatest" %% "scalatest" % "3.2.13" % Test,
      "org.scalatestplus" %% "scalacheck-1-15" % "3.2.12.0-RC2" % Test,
      "com.h2database" % "h2" % "2.1.214" % Test
    ),
      scalaSource in Test := baseDirectory.value / "src/test/scala"

)
