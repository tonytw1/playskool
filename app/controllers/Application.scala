package controllers

import play.api.mvc._
import services.WidgetService

import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits.defaultContext

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
    Future[String]("Blah")
  }

}