package services.tagging

import model.{Newsworthy, Tag}

import scala.concurrent.ExecutionContext.Implicits.{global => ec}

trait Tags {

  val availableTags = Seq(Tag("rugby", "Rugby", None), Tag("vuw", "Victoria University", None),
    Tag("upper-hutt", "Upper Hutt", None), Tag("soccer", "Soccer", Some("football")))

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