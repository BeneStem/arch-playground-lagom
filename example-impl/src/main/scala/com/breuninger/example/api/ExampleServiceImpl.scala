package com.breuninger.example.api

import com.breuninger.example.domain.Example
import com.lightbend.lagom.scaladsl.api.ServiceCall

import scala.concurrent.Future

class ExampleServiceImpl() extends ExampleService {

  override def getExample(id: String) = ServiceCall { _ =>
    Future.successful(Example(id))
  }
}
