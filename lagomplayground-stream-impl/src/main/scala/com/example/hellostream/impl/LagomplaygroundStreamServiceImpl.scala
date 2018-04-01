package com.example.hellostream.impl

import com.example.hello.api.LagomplaygroundService
import com.example.hellostream.api.LagomplaygroundStreamService
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

class LagomplaygroundStreamServiceImpl(lagomplaygroundService: LagomplaygroundService) extends LagomplaygroundStreamService {
  def stream = ServiceCall { hellos =>
    Future.successful(hellos.mapAsync(8)(lagomplaygroundService.hello(_).invoke()))
  }
}
