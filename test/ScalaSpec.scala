import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.Helpers._
import play.api.test._

@RunWith(classOf[JUnitRunner])
class ScalaSpec extends Specification {

  "Scala" should {

    "folding options" in new WithApplication{

      val set: Option[String] = Some("a value")
      val notSet: Option[String] = None

      set.getOrElse("default value") must be ("a value")
      notSet.getOrElse("default value") must be ("default value")

      set.fold("default value")(v => v) must be ("a value")
      notSet.fold("default value")(v => v) must be ("default value")
    }

  }

}