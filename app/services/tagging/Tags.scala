package services.tagging

import model.{Newsworthy, Tag}

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait Tags {

  val tagService: TagService = TagService

  def byId(id: Int): Future[Option[Tag]] = {
    all.map(ts =>
      ts.find(t => {
        t.id.equals(id)
      }
    ))
  }

  def all(): Future[Seq[Tag]] = {
   tagService.fetchTags()
  }

}

object Tags extends Tags