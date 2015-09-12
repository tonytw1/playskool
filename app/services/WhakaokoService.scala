package services

import model.{Newsitem, Tag, FeedItem}
import play.api.Logger
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait WhakaokoService {

  val url: String = "https://whakaoko.eelpieconsulting.co.uk/tonytw1/channels/wellynews/items"

  val availableTags = Seq(Tag("Rugby"), Tag("Victoria University"), Tag("Upper Hutt"))

  def fetchFeed(): Future[Seq[Newsitem]] = {
    Logger.info("Fetching from url: " + url)
    WS.url(url).get.map {
      response => {
        val feedItems: Seq[FeedItem] = Json.parse(response.body).as[Seq[FeedItem]]
        feedItems.map(f => {
          val tags: Seq[Tag] = inferTagsFor(f)
          new Newsitem(f.title, f.url, f.imageUrl, f.body, tags)
        })
      }
    }
  }

  def inferTagsFor(item: FeedItem): Seq[Tag] = {
    availableTags.filter(t => {
      item.title.toLowerCase.contains(t.name.toLowerCase)
    })
  }

}

object WhakaokoService extends WhakaokoService