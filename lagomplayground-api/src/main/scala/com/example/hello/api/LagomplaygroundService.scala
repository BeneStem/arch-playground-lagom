package com.example.hello.api

import akka.{Done, NotUsed}
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.api.broker.kafka.{KafkaProperties, PartitionKeyStrategy}
import com.lightbend.lagom.scaladsl.api.{Descriptor, Service, ServiceCall}
import play.api.libs.json.{Format, Json}

object LagomplaygroundService {
  val TOPIC_NAME = "greetings"
}

trait LagomplaygroundService extends Service {

  def hello(id: String): ServiceCall[NotUsed, String]

  def useGreeting(id: String): ServiceCall[GreetingMessage, Done]

  def greetingsTopic(): Topic[GreetingMessageChanged]

  override final def descriptor: Descriptor = {
    import Service._
    named("lagomplayground")
      .withCalls(
        pathCall("/api/hello/:id", hello _),
        pathCall("/api/hello/:id", useGreeting _)
      )
      .withTopics(
        topic(LagomplaygroundService.TOPIC_NAME, greetingsTopic())
          .addProperty(
            KafkaProperties.partitionKeyStrategy,
            PartitionKeyStrategy[GreetingMessageChanged](_.name)
          )
      )
      .withAutoAcl(true)
  }
}

case class GreetingMessage(message: String)

object GreetingMessage {
  implicit val format: Format[GreetingMessage] = Json.format[GreetingMessage]
}

case class GreetingMessageChanged(name: String, message: String)

object GreetingMessageChanged {
  implicit val format: Format[GreetingMessageChanged] = Json.format[GreetingMessageChanged]
}
