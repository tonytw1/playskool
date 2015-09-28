package services.tagging

import model.{Newsworthy, Tag}
import services.mongo.MongoService

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait Tags {

  val tagService: TagService = TagService
  val mongoService: MongoService = MongoService

  def byId(id: Int): Future[Option[Tag]] = {
    all.map(ts =>
      ts.find(t => {
        t.id.equals(id)
      }
    ))
  }

  def all(): Future[Seq[Tag]] = {
    mongoService.readTags()
  }

  def fetch(): Future[Seq[Tag]] = {
    val tags: Future[Seq[Tag]] = tagService.fetchTags()
    tags.map(ts => ts.foreach(t => mongoService.writeTag(t)))
    tags
  }

}

object Tags extends Tags