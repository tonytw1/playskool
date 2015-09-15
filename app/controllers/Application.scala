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
    Tags.all.map(t => Ok(views.html.tags(t)))
  }

  def tag(id: String) = Action.async {
    val ft: Future[Option[Tag]] = tags.byId(id)

    ft.map(to => to.fold(NotFound("Not found"))(t => tagPage(t)))



  }

  private def tagPage(tag: Tag): Future[Result] = {
    newsItemService.tagged(tag).map(ns =>
      Ok(views.html.tag(ns, tag)))
  }

}