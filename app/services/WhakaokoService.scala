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

  val tag: Tag = Tag("Some tag")

  def fetchFeed(): Future[Seq[Newsitem]] = {
    Logger.info("Fetching from url: " + url)
    WS.url(url).get.map {
      response => {
        val feedItems: Seq[FeedItem] = Json.parse(response.body).as[Seq[FeedItem]]
        feedItems.map(f => {
          new Newsitem(f.title, f.url, f.imageUrl, f.body, Seq(tag))
        })
      }
    }
  }

}

object WhakaokoService extends WhakaokoService