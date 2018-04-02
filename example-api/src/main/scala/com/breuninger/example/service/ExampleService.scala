package com.breuninger.example.service

import akka.NotUsed
import com.breuninger.example.domain.Example
import com.lightbend.lagom.scaladsl.api.transport.Method
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}

trait ExampleService extends Service {

  def example(id: String): ServiceCall[NotUsed, Example]

  override final def descriptor: Descriptor = {
    import Service._
    named("example")
      .withCalls(
        restCall(Method.GET, "/api/example/:id", example _)
      )
  }
}
