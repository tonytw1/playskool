package services.tagging

import model.Tag
import play.api.Logger
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait TagService {

  val url: String = "http://wellington.gen.nz/tags/json"

  def fetchTags(): Future[Seq[Tag]] = {
    Logger.info("Fetching from url: " + url)
    WS.url(url).get.map {
      response => {
        Json.parse(response.body).as[Seq[Tag]]
      }
    }
  }

}

object TagService extends TagService