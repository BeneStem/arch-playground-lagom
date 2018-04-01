organization in ThisBuild := "com.example"
version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.5"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"
val scalaTest = "org.scalatest" %% "scalatest" % "3.0.4" % Test

lazy val `lagomplayground` = (project in file("."))
  .aggregate(`lagomplayground-api`, `lagomplayground-impl`, `lagomplayground-stream-api`, `lagomplayground-stream-impl`)

lazy val `lagomplayground-api` = (project in file("lagomplayground-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagomplayground-impl` = (project in file("lagomplayground-impl"))
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
  .dependsOn(`lagomplayground-api`)

lazy val `lagomplayground-stream-api` = (project in file("lagomplayground-stream-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `lagomplayground-stream-impl` = (project in file("lagomplayground-stream-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslTestKit,
      macwire,
      scalaTest
    )
  )
  .dependsOn(`lagomplayground-stream-api`, `lagomplayground-api`)
