package controllers

import play.api.libs.json.JsValue
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
    val future: Future[JsValue] = tflService.fetchData()
    val result: Future[Result] = future.map { data =>
      val commonName: String = data.\("commonName").as[String]
      Ok(views.html.ws(commonName))}
    result
  }

}