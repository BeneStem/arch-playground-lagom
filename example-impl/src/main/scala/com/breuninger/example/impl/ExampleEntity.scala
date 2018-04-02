package com.breuninger.example.impl

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq

class ExampleEntity extends PersistentEntity {
  override type Command = ExampleCommand
  override type Event = ExampleEvent
  override type State = Option[Example]

  override def initialState: None.type = None

  override def behavior: Behavior = {
    case Some(_) =>
      Actions().onReadOnlyCommand[GetExample, Option[Example]] {
        case (GetExample(_), ctx, state) => ctx.reply(state)
      }.onReadOnlyCommand[CreateExample, Done] {
        case (CreateExample(_), ctx, _) => ctx.invalidCommand("User already exists")
      }
    case None =>
      Actions().onReadOnlyCommand[GetExample, Option[Example]] {
        case (GetExample(_), ctx, state) => ctx.reply(state)
      }.onCommand[CreateExample, Done] {
        case (CreateExample(text), ctx, _) => ctx.thenPersist(ExampleCreated(text))(_ => ctx.reply(Done))
      }.onEvent {
        case (ExampleCreated(text), _) => Some(Example(text))
      }
  }
}

case class Example(text: String)

object Example {
  implicit val format: Format[Example] = Json.format
}

sealed trait ExampleEvent

case class ExampleCreated(text: String) extends ExampleEvent

object ExampleCreated {
  implicit val format: Format[ExampleCreated] = Json.format
}

sealed trait ExampleCommand

case class CreateExample(text: String) extends ExampleCommand with ReplyType[Done]

object CreateExample {
  implicit val format: Format[CreateExample] = Json.format
}

case class GetExample(id: String) extends ExampleCommand with ReplyType[Option[Example]]

case object GetExample {
  implicit val format: Format[GetExample] = Json.format
}

object ExampleSerializerRegistry extends JsonSerializerRegistry {
  override def serializers = Seq(
    JsonSerializer[Example],
    JsonSerializer[ExampleCreated],
    JsonSerializer[CreateExample],
    JsonSerializer[GetExample]
  )
}
