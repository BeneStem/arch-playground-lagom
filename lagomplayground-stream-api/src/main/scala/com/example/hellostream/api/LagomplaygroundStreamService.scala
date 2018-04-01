package com.example.hellostream.api

import akka.NotUsed
import akka.stream.scaladsl.Source
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait LagomplaygroundStreamService extends Service {

  def stream: ServiceCall[Source[String, NotUsed], Source[String, NotUsed]]

  override final def descriptor: Descriptor = {
    import Service._

    named("lagomplayground-stream")
      .withCalls(
        namedCall("stream", stream)
      ).withAutoAcl(true)
  }
}
