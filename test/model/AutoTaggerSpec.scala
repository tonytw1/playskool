package model

import org.specs2.mutable._
import services.tagging.AutoTagger

class AutoTaggerSpec extends Specification {

  val availableTags = Seq(Tag(1, "Rugby", None), Tag(2, "Victoria University", None),
    Tag(3, "Upper Hutt", None), Tag(4, "Soccer", Some("football")))

  "Auto tagger" should {

    val autoTagger = AutoTagger
    "match tag names in feeditem titles" in {

      val tags = autoTagger.inferTagsFor(FeedItem("Something mentioning Rugby", "http://localhost/123", None, None, None), availableTags)

      tags must have length(1)
      tags.head.name must be ("Rugby")
    }

    "be case insensitive when matching tag names in titles" in {

      val tags = autoTagger.inferTagsFor(FeedItem("Something mentioning rugby", "http://localhost/123", None, None, None), availableTags)

      tags must have length(1)
      tags.head.name must be ("Rugby")
    }

    "respond to autotagging hints" in {
      val tags = autoTagger.inferTagsFor(FeedItem("Something mentioning football", "http://localhost/123", None, None, None), availableTags)

      tags must have length(1)
      tags.head.name must be ("Soccer")
    }

  }

}