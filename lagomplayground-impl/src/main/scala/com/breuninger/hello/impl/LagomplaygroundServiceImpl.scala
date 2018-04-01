package com.breuninger.hello.impl

import akka.Done
import com.breuninger.hello.api.LagomplaygroundService
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

class LagomplaygroundServiceImpl() extends LagomplaygroundService {

  override def hello(id: String) = ServiceCall { _ =>
    Future.successful(id)
  }
}
