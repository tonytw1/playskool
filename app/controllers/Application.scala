package controllers

import model.{Newsitem, Tag}
import play.api.mvc._
import services.mongo.MongoService
import services.newsitems.NewsitemService
import services.tagging.Tags

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends Controller {

  val newsItemService: NewsitemService = NewsitemService
  val tags: Tags = Tags
  val mongoService: MongoService = MongoService

  def index = Action.async {
    newsItemService.latest.map(ns =>
      ns.map(n => mongoService.write(n))
    )

    // Future write not actually complete yet

    mongoService.listDocs().map(ns =>
      Ok(views.html.homepage(ns))
    )
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