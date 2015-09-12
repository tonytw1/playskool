package services.tagging

import model.{Newsworthy, Tag}

import scala.concurrent.ExecutionContext.Implicits.{global => ec}

trait AutoTagger {

  val availableTags = Seq(Tag("Rugby"), Tag("Victoria University"), Tag("Upper Hutt"))

  def inferTagsFor(item: Newsworthy): Seq[Tag] = {
    availableTags.filter(t => {
      item.title.toLowerCase.contains(t.name.toLowerCase)
    })
  }

}

object AutoTagger extends AutoTagger