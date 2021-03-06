import com.breuninger.example.api.ExampleService
import com.lightbend.lagom.scaladsl.api.{LagomConfigComponent, ServiceAcl, ServiceInfo}
import com.lightbend.lagom.scaladsl.client.LagomServiceClientComponents
import com.lightbend.lagom.scaladsl.devmode.LagomDevModeComponents
import com.lightbend.rp.servicediscovery.lagom.scaladsl.LagomServiceLocatorComponents
import com.softwaremill.macwire._
import controllers.{AssetsComponents, Main}
import play.api.ApplicationLoader.Context
import play.api.libs.ws.ahc.AhcWSComponents
import play.api.routing.Router
import play.api.{Application, ApplicationLoader, BuiltInComponentsFromContext, Mode}
import play.filters.HttpFiltersComponents
import router.Routes

import scala.collection.immutable
import scala.concurrent.ExecutionContext

class WebGatewayLoader extends ApplicationLoader {
  override def load(context: Context): Application = context.environment.mode match {
    case Mode.Dev =>
      (new WebGatewayApplication(context) with LagomDevModeComponents).application
    case _ =>
      (new WebGatewayApplication(context) with LagomServiceLocatorComponents).application
  }
}

abstract class WebGatewayApplication(context: Context) extends BuiltInComponentsFromContext(context)
  with LagomConfigComponent
  with LagomServiceClientComponents
  with AhcWSComponents
  with AssetsComponents
  with HttpFiltersComponents {

  override lazy val serviceInfo: ServiceInfo = ServiceInfo("web-gateway",
    Map("web-gateway" -> immutable.Seq(ServiceAcl.forPathRegex("(?!/api/).*"))))
  override implicit lazy val executionContext: ExecutionContext = actorSystem.dispatcher

  override lazy val router: Router = {
    val prefix = "/arch-playground-lagom"
    wire[Routes]
  }

  lazy val exampleService: ExampleService = serviceClient.implement[ExampleService]

  lazy val main: Main = wire[Main]
}
