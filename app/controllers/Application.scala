package controllers

import play.api.mvc._
import services.newsitems.NewsitemService

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  val newsItemService: NewsitemService = NewsitemService

  def index = Action.async {
    newsItemService.latest.map(ns =>
      Ok(views.html.homepage(ns)
    ))
  }

}