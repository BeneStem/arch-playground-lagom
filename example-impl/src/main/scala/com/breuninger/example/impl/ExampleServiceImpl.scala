package com.breuninger.example.impl

import java.util.UUID

import com.breuninger.example.api
import com.breuninger.example.api.ExampleService
import com.lightbend.lagom.scaladsl.api.ServiceCall
import com.lightbend.lagom.scaladsl.api.transport.NotFound
import com.lightbend.lagom.scaladsl.persistence.PersistentEntityRegistry

import scala.concurrent.ExecutionContext

class ExampleServiceImpl(persistentEntityRegistry: PersistentEntityRegistry)(implicit executionContext: ExecutionContext) extends ExampleService {

  override def createExample = ServiceCall { createExample =>
    val id = UUID.randomUUID().toString
    refFor(id).ask(CreateExample(createExample.text)).map { _ =>
      api.Example(id, createExample.text)
    }
  }

  override def getExample(id: String) = ServiceCall { _ =>
    refFor(id).ask(GetExample).map {
      case Some(example) =>
        api.Example(id, example.text)
      case None =>
        throw NotFound(s"Example with id $id")
    }
  }

  private def refFor(id: String) = persistentEntityRegistry.refFor[ExampleEntity](id)
}
