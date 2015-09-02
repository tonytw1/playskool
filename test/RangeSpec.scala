import org.specs2.mutable.Specification
import play.api.test._

class RangeSpec extends Specification {

  "Ranges" should {

    "Can create a list of labels from a range of ints" in new WithApplication {

      private val range = 1 to 25
      private val labels = range map (f => {
        "N" + f.toString
      })

      println(labels) // TODO asserts
    }

    "Can create a list of labels from a range of chars" in new WithApplication {
      private val labels = ('A' to 'Z' map (_.toString)).toIterator
      println(labels) // TODO asserts
    }

  }

}
