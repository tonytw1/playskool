package services.tagging

import model.{Newsworthy, Tag}

import scala.concurrent.ExecutionContext.Implicits.{global => ec}

trait AutoTagger {

  def inferTagsFor(item: Newsworthy, availableTags: Seq[Tag]): Seq[Tag] = {
    availableTags.filter(t => {
      val headlineMatches = item.title.toLowerCase.contains(t.name.toLowerCase)
      val autotagHintsMatch = t.autoTagHints.fold(false)(h => item.title.toLowerCase.contains(h))

      headlineMatches || autotagHintsMatch
    })
  }

}

object AutoTagger extends AutoTagger