package com.example.hello.impl

import com.example.hello.api
import com.example.hello.api.LagomplaygroundService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.broker.Topic
import com.lightbend.lagom.scaladsl.broker.TopicProducer
import com.lightbend.lagom.scaladsl.persistence.{EventStreamElement, PersistentEntityRegistry}

class LagomplaygroundServiceImpl(persistentEntityRegistry: PersistentEntityRegistry) extends LagomplaygroundService {

  override def hello(id: String) = ServiceCall { _ =>
    val ref = persistentEntityRegistry.refFor[LagomplaygroundEntity](id)

    ref.ask(Hello(id))
  }

  override def useGreeting(id: String) = ServiceCall { request =>
    val ref = persistentEntityRegistry.refFor[LagomplaygroundEntity](id)

    ref.ask(UseGreetingMessage(request.message))
  }

  override def greetingsTopic(): Topic[api.GreetingMessageChanged] =
    TopicProducer.singleStreamWithOffset {
      fromOffset =>
        persistentEntityRegistry.eventStream(LagomplaygroundEvent.Tag, fromOffset)
          .map(ev => (convertEvent(ev), ev.offset))
    }

  private def convertEvent(helloEvent: EventStreamElement[LagomplaygroundEvent]): api.GreetingMessageChanged = {
    helloEvent.event match {
      case GreetingMessageChanged(msg) => api.GreetingMessageChanged(helloEvent.entityId, msg)
    }
  }
}
