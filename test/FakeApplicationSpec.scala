import org.junit.runner._
import org.scalatest.{OptionValues, EitherValues, Matchers, WordSpec}
import org.specs2.mutable._
import org.specs2.runner._
import play.api.test.{FakeApplication, WithApplication}

class FakeApplicationSpec extends WordSpec with Matchers with EitherValues with OptionValues {

  val fa = FakeApplication(additionalConfiguration = Map(
    "logger.root" -> "TRACE",
    "logger.play" -> "TRACE",
    "logger.application" -> "TRACE"
  ))

  "Application" should {

    "work from within a fake application" in new WithApplication(fa) {
      println("Done")
    }
    "work from within a fake application2" in new WithApplication(fa) {
      println("Done")
    }
    "work from within a fake application3" in new WithApplication(fa) {
      println("Done")
    }
    "work from within a fake application4" in new WithApplication(fa) {
      println("Done")
    }
    "work from within a fake application5" in new WithApplication(fa) {
      println("Done")
    }
    "work from within a fake application6" in new WithApplication(fa) {
      println("Done")
    }
    "work from within a fake application7" in new WithApplication(fa) {
      println("Done")
    }
  }
  
}
