package com.example.hello.impl

import com.example.hello.api.LagomplaygroundService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.broker.kafka.LagomKafkaComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.persistence.cassandra.CassandraPersistenceComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class LagomplaygroundLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomplaygroundApplication(context) {
      override def serviceLocator: ServiceLocator = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomplaygroundApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomplaygroundService])
}

abstract class LagomplaygroundApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with CassandraPersistenceComponents
    with LagomKafkaComponents
    with AhcWSComponents {

  override lazy val lagomServer: LagomServer = serverFor[LagomplaygroundService](wire[LagomplaygroundServiceImpl])

  override lazy val jsonSerializerRegistry: LagomplaygroundSerializerRegistry.type = LagomplaygroundSerializerRegistry

  persistentEntityRegistry.register(wire[LagomplaygroundEntity])
}
