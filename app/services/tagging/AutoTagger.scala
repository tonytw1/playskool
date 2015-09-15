package services.tagging

import model.{Newsworthy, Tag}

import scala.concurrent.ExecutionContext.Implicits.{global => ec}

trait AutoTagger {

  val tags: Tags = Tags

  def inferTagsFor(item: Newsworthy): Seq[Tag] = {
    val headlineBasedMatches: Seq[Tag] = tags.all.filter(t => {
      item.title.toLowerCase.contains(t.name.toLowerCase)
    })
    headlineBasedMatches
  }

}

object AutoTagger extends AutoTagger