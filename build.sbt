organization in ThisBuild := "com.breuninger"
version in ThisBuild := "1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.5"

val macwire = "com.softwaremill.macwire" %% "macros" % "2.3.0" % "provided"

lazy val `lagomplayground` = (project in file("."))
  .aggregate(`lagomplayground-api`, `lagomplayground-impl`)

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
      macwire
    )
  )
  .settings(lagomForkedTestSettings: _*)
  .dependsOn(`lagomplayground-api`)

lagomCassandraEnabled in ThisBuild := false
lagomKafkaEnabled in ThisBuild := false
