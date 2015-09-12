package services

import model.FeedItem
import play.api.Logger
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait WhakaokoService {

  val url: String = "https://whakaoko.eelpieconsulting.co.uk/tonytw1/channels/wellynews/items"

  def fetchFeed(): Future[Seq[FeedItem]] = {
    Logger.info("Fetching from url: " + url)
    WS.url(url).get.map {
      response => {
        Json.parse(response.body).as[Seq[FeedItem]]
      }
    }
  }

}

object WhakaokoService extends WhakaokoService