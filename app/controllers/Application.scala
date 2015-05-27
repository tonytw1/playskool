package controllers

import model.Widget
import play.api.mvc._
import services.WidgetService

object Application extends Controller {

  val widgetService: WidgetService = new WidgetService

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

}