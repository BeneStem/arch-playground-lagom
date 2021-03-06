organization in ThisBuild := "com.breuninger"
name in ThisBuild := "arch-playground-lagom"
version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.6"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"

lazy val `arch-playground-lagom` = (project in file("."))
  .aggregate(`common`, `example-api`, `example-impl`, `web-gateway`)

lazy val common = (project in file("common"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )

lazy val `example-api` = (project in file("example-api"))
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslApi
    )
  )
  .dependsOn(common)

lazy val `example-impl` = (project in file("example-impl"))
  .enablePlugins(LagomScala, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslPersistenceCassandra,
      macwire
    )
  )
  .dependsOn(`example-api`)

lazy val `web-gateway` = (project in file("web-gateway"))
  .enablePlugins(PlayScala, LagomPlay, SbtReactiveAppPlugin)
  .settings(
    libraryDependencies ++= Seq(
      lagomScaladslServer,
      macwire
    )
  )
  .dependsOn(`example-api`)

lagomCassandraCleanOnStart in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false
