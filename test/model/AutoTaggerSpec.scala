package model

import org.specs2.mutable._
import services.tagging.AutoTagger

class AutoTaggerSpec extends Specification {

  "Auto tagger" should {

    val autoTagger = AutoTagger
    "match tag names in feeditem titles" in {

      val tags = autoTagger.inferTagsFor(FeedItem("Something mentioning Rugby", "http://localhost/123", None, None))

      tags must have length(1)
      tags must contain(Tag("rugby", "Rugby"))
    }

    "be case insenitice when matching tag names in titles" in {

      val tags = autoTagger.inferTagsFor(FeedItem("Something mentioning rugby", "http://localhost/123", None, None))

      tags must have length(1)
      tags must contain(Tag("rugby", "Rugby"))
    }

  }

}