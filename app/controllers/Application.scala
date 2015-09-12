package controllers

import model.{Newsitem, FeedItem}
import play.api.mvc._
import services.tagging.AutoTagger
import services.{WhakaokoService, TFLService}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Application extends Controller {

  val whakaokoService: WhakaokoService = WhakaokoService
  val autoTagger: AutoTagger = AutoTagger

  def index = Action.async {
    val feedItemsFuture: Future[Seq[FeedItem]] = whakaokoService.fetchFeed()

    feedItemsFuture.map(feedItems => {

      val newsitems: Seq[Newsitem] = feedItems.map(i => {
        Newsitem(i.title, i.url, i.imageUrl, i.body, autoTagger.inferTagsFor(i))
      })

      Ok(views.html.homepage(newsitems))
    })
  }

}