package com.example.hellostream.impl

import com.example.hello.api.LagomplaygroundService
import com.example.hellostream.api.LagomplaygroundStreamService
import com.lightbend.lagom.scaladsl.api.ServiceLocator
import com.lightbend.lagom.scaladsl.api.ServiceLocator.NoServiceLocator
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.lagom.scaladsl.server._
import com.softwaremill.macwire._
import play.api.libs.ws.ahc.AhcWSComponents

class LagomplaygroundStreamLoader extends LagomApplicationLoader {

  override def load(context: LagomApplicationContext): LagomApplication =
    new LagomplaygroundStreamApplication(context) {
      override def serviceLocator: ServiceLocator.NoServiceLocator.type = NoServiceLocator
    }

  override def loadDevMode(context: LagomApplicationContext): LagomApplication =
    new LagomplaygroundStreamApplication(context) with LagomDevModeComponents

  override def describeService = Some(readDescriptor[LagomplaygroundStreamService])
}

abstract class LagomplaygroundStreamApplication(context: LagomApplicationContext)
  extends LagomApplication(context)
    with AhcWSComponents {

  override lazy val lagomServer: LagomServer = serverFor[LagomplaygroundStreamService](wire[LagomplaygroundStreamServiceImpl])

  lazy val lagomplaygroundService: LagomplaygroundService = serviceClient.implement[LagomplaygroundService]
}
