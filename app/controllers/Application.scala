package controllers

import model.Tag
import play.api.mvc._
import services.newsitems.NewsitemService
import services.tagging.Tags
import scala.concurrent.Future

import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  val newsItemService: NewsitemService = NewsitemService
  val tags: Tags = Tags

  def index = Action.async {
    newsItemService.latest.map(ns =>
      Ok(views.html.homepage(ns)
    ))
  }

  def allTags = Action.async {
    Future.successful(Ok(views.html.tags(Tags.all())))
  }

  def tag(id: String) = Action.async {
    val tag: Tag = tags.byId(id)
    newsItemService.tagged(tag).map(ns =>
      Ok(views.html.tag(ns, tag)))
  }

}