import org.specs2.mutable.Specification
import play.api.test._
import play.api.test._

class OptionSpec extends Specification {

  "Options" should {

    "Fold on an option lets you run functions on both sides" in new WithApplication {

      Some("Meh").fold("Nothing")(x => "Found "  + x) must equalTo("Found Meh")
      None.fold("Nothing")(x => "Found "  + x) must equalTo("Nothing")

    }

  }

}
