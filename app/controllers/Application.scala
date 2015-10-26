package controllers

import model.{Newsitem, Tag}
import play.api.{Play, Logger}
import play.api.mvc._
import services.mongo.MongoService
import services.newsitems.NewsitemService
import services.tagging.Tags

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.Play.current

object Application extends Controller {

  val newsItemService: NewsitemService = NewsitemService
  val tags: Tags = Tags
  val mongoService: MongoService = MongoService

  lazy val configValue: String = Play.configuration.getString("config.value").get
  lazy val configValues: Seq[String] = Play.configuration.getStringSeq("config.values").get

  def index = Action.async {
    Logger.info("Config value: " + configValue)
    Logger.info("Config values: " + configValues)
    Logger.info("Config values size: " + configValues.size)

    mongoService.read().map(ns =>
      Ok(views.html.homepage(ns))
    )
  }

  def fetch = Action.async {
    newsItemService.latest.map(ns => {
      ns.map(n => mongoService.write(n))
      Ok(views.html.homepage(ns))
    })
  }

  def fetchTags = Action.async {
    Tags.fetch.map(t => Ok(views.html.tags(t)))
  }

  def allTags = Action.async {
    Tags.all.map(t => Ok(views.html.tags(t)))
  }

  def tag(id: Int) = Action.async {
    tags.byId(id).flatMap(tag =>
      tag.fold(Future.successful(NotFound("Not found")))(t => tagPage(t)))
  }

  private def tagPage(tag: Tag) = {
    mongoService.readByTag(tag).map(ns => {
      Ok(views.html.tag(ns, tag))
    })
  }

}