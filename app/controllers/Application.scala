package controllers

import play.api.libs.ws.{WSResponse, WSRequestHolder, WS}
import play.api.mvc._
import services.WidgetService
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

import scala.concurrent.Future

object Application extends Controller {

  val widgetService: WidgetService = WidgetService

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def foo = Action {
    Ok(views.html.foo())
  }

  def meh = Action {
    val widget = widgetService.buildWidget
    Ok(views.html.meh("world", widget))
  }

  def ws = Action.async {
    val future: Future[String] = fetchData()
    val result: Future[Result] = future.map { data => Ok("Got data: " + data)}
    result
  }

  private def fetchData(): Future[String] = {
    val holder: WSRequestHolder = WS.url("http://api.prod5.live.tfl.gov.uk/Place/BikePoints_195")
    val eventualResponse: Future[WSResponse] = holder.get

    val result: Future[String] = eventualResponse.map {
      response => response.body
    }
    result
  }

}