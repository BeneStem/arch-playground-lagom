organization in ThisBuild := "com.breuninger"
name in ThisBuild := "arch-playground-lagom"
version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.5"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"

lazy val `arch-playground-lagom` = (project in file("."))
  .aggregate(`example-api`, `example-impl`)

lazy val `example-api` = (project in file("example-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `example-impl` = (project in file("example-impl"))
  .enablePlugins(LagomScala)
  .settings(
    libraryDependencies ++= Seq(
      macwire
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`example-api`)

lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false
