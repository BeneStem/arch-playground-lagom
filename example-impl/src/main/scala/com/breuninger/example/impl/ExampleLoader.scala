package com.breuninger.example.impl

import com.breuninger.example.api.ExampleService
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class ExampleLoader extends LagomApplicationLoader {
  override def load(context: LagomApplicationContext) = new ExampleApplication(context) with LagomServiceLocatorComponents

  override def loadDevMode(context: LagomApplicationContext) = new ExampleApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[ExampleService])
}

abstract class ExampleApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents
    with CassandraPersistenceComponents {

  override lazy val lagomServer: LagomServer = serverFor[ExampleService](wire[ExampleServiceImpl])
  override lazy val jsonSerializerRegistry: ExampleSerializerRegistry.type = ExampleSerializerRegistry

  persistentEntityRegistry.register(wire[ExampleEntity])
}
