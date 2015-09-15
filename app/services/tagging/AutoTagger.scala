package services.tagging

import model.{Newsworthy, Tag}

import scala.concurrent.ExecutionContext.Implicits.{global => ec}

trait AutoTagger {

  val tags: Tags = Tags

  def inferTagsFor(item: Newsworthy): Seq[Tag] = {
    tags.all.filter(t => {
      val headlineMatches = item.title.toLowerCase.contains(t.name.toLowerCase)
      val autotagHintsMatch = t.autoTagHints.fold(false)(h => item.title.toLowerCase.contains(h))

      headlineMatches || autotagHintsMatch
    })
  }

}

object AutoTagger extends AutoTagger