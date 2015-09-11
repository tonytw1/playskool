package controllers

import model.{Newsitem, FeedItem}
import play.api.mvc._
import services.{WhakaokoService, TFLService}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends Controller {

  val whakaokoService: WhakaokoService = WhakaokoService

  def index = Action.async {
    val feedItemsFuture: Future[Seq[Newsitem]] = whakaokoService.fetchFeed()
    feedItemsFuture.map(f => Ok(views.html.homepage(f)))
  }

}