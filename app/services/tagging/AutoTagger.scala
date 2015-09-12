package services.tagging

import model.{Newsitem, Tag, FeedItem}
import play.api.Logger
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait AutoTagger {

  val availableTags = Seq(Tag("Rugby"), Tag("Victoria University"), Tag("Upper Hutt"))

  def inferTagsFor(item: FeedItem): Seq[Tag] = {
    availableTags.filter(t => {
      item.title.toLowerCase.contains(t.name.toLowerCase)
    })
  }

}

object AutoTagger extends AutoTagger