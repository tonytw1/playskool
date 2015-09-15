package services.newsitems

import model.{FeedItem, Newsitem, Tag}
import services.WhakaokoService
import services.tagging.{Tags, AutoTagger}

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait NewsitemService {

  val whakaokoService: WhakaokoService = WhakaokoService
  val autoTagger: AutoTagger = AutoTagger
  val tags: Tags = Tags

  def latest(): Future[Seq[Newsitem]] = {

    val feedItemsFuture: Future[Seq[FeedItem]] = whakaokoService.fetchFeed()
    val tagsFuture: Future[Seq[Tag]] = tags.all()

    for {
      ts <- tagsFuture.map(t => t)
      fis <- feedItemsFuture.map(f => f)

    } yield (
      fis.map(i => {
        Newsitem(i.title, i.url, i.imageUrl, i.body, i.date, autoTagger.inferTagsFor(i, ts))
      }))

  }

  def tagged(tag: Tag): Future[Seq[Newsitem]] = {
    latest().map(ns => {
      ns.filter(i => i.tags.contains(tag))
    })
  }

}

object NewsitemService extends NewsitemService
