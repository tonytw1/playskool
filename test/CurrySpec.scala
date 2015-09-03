import org.specs2.mutable.Specification
import play.api.test._
import play.api.test._

class CurrySpec extends Specification {

  "Scala" should {

    "Allow functions to be assigned to vals" in new WithApplication {
      val function = (x: Int) => x + 1

      function(1) must equalTo(2)
    }

    "Allow functions to be defined using other functions" in new WithApplication {

      val a = (x: Int) => x + 1
      val b = (x: Int) => x * 2

      val c = (x: Int) => b(a(x))

      c(3) must equalTo(8)
    }

    "Can pass chains if functions" in new WithApplication {
      val intToString = (x: Int) => x.toString

      private val func: (Int) => (String) => Long = intToString => s => stringToLong(s) // TODO explain this placeholder
      println("TM: " + wtf(func))

      wtf(func) must equalTo(4)
    }

  }

  def stringToLong(s: String): Long = {
    println("String to long: " + s)
    s.toLong
  }

  def wtf(func: Int => String => Long): Long = {
    val body1 = func(4)
    body1("4")
  }

}
