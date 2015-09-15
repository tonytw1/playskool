package controllers

import model.{Newsitem, Tag}
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
    Tags.all.map(t => Ok(views.html.tags(t)))
  }

  def tag(id: Int) = Action.async {
    tags.byId(id).flatMap(tag =>
      tag.fold(Future.successful(NotFound("Not found")))(t => tagPage(t)))
  }

  private def tagPage(tag: Tag) = {
    newsItemService.tagged(tag).map(ns => {
      Ok(views.html.tag(ns, tag))
    })
  }

}