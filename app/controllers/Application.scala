package controllers

import play.api.mvc._
import services.{TFLService, WidgetService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends Controller {

  val tflService: TFLService = TFLService
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
    val future: Future[String] = tflService.fetchData()
    val result: Future[Result] = future.map { data => Ok(data)}
    result
  }

}