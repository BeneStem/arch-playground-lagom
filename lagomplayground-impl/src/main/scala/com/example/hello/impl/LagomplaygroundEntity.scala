package com.example.hello.impl

import java.time.LocalDateTime

import akka.Done
import com.lightbend.lagom.scaladsl.persistence.PersistentEntity.ReplyType
import com.lightbend.lagom.scaladsl.persistence.{AggregateEvent, AggregateEventTag, PersistentEntity}
import com.lightbend.lagom.scaladsl.playjson.{JsonSerializer, JsonSerializerRegistry}
import play.api.libs.json.{Format, Json}

import scala.collection.immutable.Seq

class LagomplaygroundEntity extends PersistentEntity {

  override type Command = LagomplaygroundCommand[_]
  override type Event = LagomplaygroundEvent
  override type State = LagomplaygroundState

  override def initialState: LagomplaygroundState = LagomplaygroundState("Hello", LocalDateTime.now.toString)

  override def behavior: Behavior = {
    case LagomplaygroundState(message, _) => Actions().onCommand[UseGreetingMessage, Done] {

      case (UseGreetingMessage(newMessage), ctx, _) =>
        ctx.thenPersist(
          GreetingMessageChanged(newMessage)
        ) { _ =>
          ctx.reply(Done)
        }
    }.onReadOnlyCommand[Hello, String] {

      case (Hello(name), ctx, _) =>
        ctx.reply(s"$message, $name!")
    }.onEvent {

      case (GreetingMessageChanged(newMessage), _) =>
        LagomplaygroundState(newMessage, LocalDateTime.now().toString)
    }
  }
}

case class LagomplaygroundState(message: String, timestamp: String)

object LagomplaygroundState {
  implicit val format: Format[LagomplaygroundState] = Json.format
}

sealed trait LagomplaygroundEvent extends AggregateEvent[LagomplaygroundEvent] {
  def aggregateTag: AggregateEventTag[LagomplaygroundEvent] = LagomplaygroundEvent.Tag
}

object LagomplaygroundEvent {
  val Tag: AggregateEventTag[LagomplaygroundEvent] = AggregateEventTag[LagomplaygroundEvent]
}

case class GreetingMessageChanged(message: String) extends LagomplaygroundEvent

object GreetingMessageChanged {

  implicit val format: Format[GreetingMessageChanged] = Json.format
}

sealed trait LagomplaygroundCommand[R] extends ReplyType[R]

case class UseGreetingMessage(message: String) extends LagomplaygroundCommand[Done]

object UseGreetingMessage {

  implicit val format: Format[UseGreetingMessage] = Json.format
}

case class Hello(name: String) extends LagomplaygroundCommand[String]

object Hello {

  implicit val format: Format[Hello] = Json.format
}

object LagomplaygroundSerializerRegistry extends JsonSerializerRegistry {
  override def serializers: Seq[JsonSerializer[_]] = Seq(
    JsonSerializer[UseGreetingMessage],
    JsonSerializer[Hello],
    JsonSerializer[GreetingMessageChanged],
    JsonSerializer[LagomplaygroundState]
  )
}
