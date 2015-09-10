package controllers

import model.FeedItem
import play.api.mvc._
import services.{WhakaokoService, TFLService}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends Controller {

  val whakaokoService: WhakaokoService = WhakaokoService

  def index = Action.async {
    val feedItemsFuture: Future[Seq[FeedItem]] = whakaokoService.fetchFeed()
    feedItemsFuture.map(f => Ok(views.html.homepage(f)))
  }

}