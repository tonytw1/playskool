import org.junit.runner._
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test._

class FakeApplicationSpec extends Specification {

  val fa = FakeApplication(additionalConfiguration = Map(
    "logger.root" -> "TRACE",
    "logger.play" -> "TRACE",
    "logger.application" -> "TRACE"
  ))

  "Application" should {

    "work from within a fake application" in new WithApplication(fa) {
      println("Done")
    }
    
  }
}
