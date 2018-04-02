package controllers

import com.breuninger.example.api.ExampleService
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext

class Main(exampleService: ExampleService, controllerComponents: ControllerComponents)
          (implicit executionContext: ExecutionContext)
  extends AbstractController(controllerComponents) {

  def index(id: String): Action[AnyContent] = Action.async { _ =>
    exampleService.getExample(id).invoke()
      .map { example => Ok(views.html.index(example)) }
  }
}
