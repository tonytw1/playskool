package services.tagging

import model.{Newsworthy, Tag}

import scala.concurrent.ExecutionContext.Implicits.{global => ec}

trait Tags {

  val availableTags = Seq(Tag("rugby", "Rugby"), Tag("vuw", "Victoria University"), Tag("upper-hutt", "Upper Hutt"))

  def byId(id: String): Option[Tag] = {
    availableTags.find(t => {
      t.id.equals(id)
    })
  }

  def all(): Seq[Tag] = {
    availableTags
  }

}

object Tags extends Tags