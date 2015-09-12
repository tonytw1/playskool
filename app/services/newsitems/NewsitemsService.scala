package services.newsitems

import model.Newsitem
import model.Tag
import services.WhakaokoService
import services.tagging.AutoTagger

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait NewsitemService {

  val whakaokoService: WhakaokoService = WhakaokoService
  val autoTagger: AutoTagger = AutoTagger

  def latest(): Future[Seq[Newsitem]] = {
    whakaokoService.fetchFeed().map(feedItems => {
      feedItems.map(i => {
        Newsitem(i.title, i.url, i.imageUrl, i.body, autoTagger.inferTagsFor(i))
      })
    })
  }

  def tagged(tag: Tag): Future[Seq[Newsitem]] = {
    latest().map(ns => {
      ns.filter(i => i.tags.contains(tag))
    })
  }

}

object NewsitemService extends NewsitemService
