package controllers

import model.Widget
import play.api.mvc._

object Application extends Controller {

  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def foo = Action {
    Ok(views.html.foo())
  }

  def meh = Action {
    val widget = new Widget(2, "Red")
    Ok(views.html.meh("world", widget))
  }

}