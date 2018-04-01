package com.breuninger.hello.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait LagomplaygroundService extends Service {

  def hello(id: String): ServiceCall[NotUsed, String]

  override final def descriptor: Descriptor = {
    import Service._
    named("lagomplayground")
      .withCalls(
        pathCall("/api/hello/:id", hello _)
      )
  }
}
