package services

import model.FeedItem
import play.api.Logger
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait WhakaokoService {

  def fetchFeed(): Future[Seq[FeedItem]] = {
    Logger.info("Fetching feed from live")
    fetchFromLive()
  }

  private def fetchFromLive(): Future[Seq[FeedItem]] = {
    val url: String = "https://whakaoko.eelpieconsulting.co.uk/tonytw1/channels/wellynews/items"
    Logger.info("Fetching from url: " + url)
    WS.url(url).get.map {
      response => {
        Logger.debug("HTTP response body: " + response.body)

        implicit val readsFeedItem: Reads[FeedItem] = Json.reads[FeedItem]

        val body: String = response.body
        Logger.info(body)
        val items: Seq[FeedItem] = Json.parse(body).as[Seq[FeedItem]]
        Logger.info(items.map(i => i.title).mkString(", "))
        items
      }
    }
  }

}

object WhakaokoService extends WhakaokoService