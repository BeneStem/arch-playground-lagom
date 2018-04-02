package com.breuninger.example.api

import akka.NotUsed
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

trait ExampleService extends Service {

  def createExample: ServiceCall[CreateExample, Example]

  def getExample(id: String): ServiceCall[NotUsed, Example]

  override final def descriptor: Descriptor = {
    import Service._
    named("example")
      .withCalls(
        pathCall("/api/examples", createExample _),
        pathCall("/api/examples/:id", getExample _)
      )
  }
}

case class Example(id: String, text: String)

object Example {
  implicit val format: Format[Example] = Json.format[Example]
}

case class CreateExample(text: String)

object CreateExample {
  implicit val format: Format[CreateExample] = Json.format
}
