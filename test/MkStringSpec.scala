import model.BikePoint
import org.specs2.mutable.Specification
import play.api.test._

class MkStringSpec extends Specification {

  "MkString" should {

    "might allow me to render a collection of things as a string" in new WithApplication {
      val points = Seq[BikePoint](new BikePoint("1", "Somewhere", Seq()), new BikePoint("2", "SomewhereElse", Seq()))

      private val output: String = points.map(p => p.id + ":" + p.commonName).mkString(" ")

      output mustEqual("1:Somewhere 2:SomewhereElse")
    }

  }

}
