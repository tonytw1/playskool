import model.{BikePointAdditionalProperty, BikePoint}
import org.specs2.mutable.Specification
import play.api.libs.json._
import play.api.test._

class JsonSpec extends Specification {

  "Play JSON library" should {

    "Round trip simple domain objects" in new WithApplication {
      implicit val reads: Format[BikePoint] = Json.format[BikePoint]
      implicit val bikePointAdditionalPropertyReads: Format[BikePointAdditionalProperty]= Json.format[BikePointAdditionalProperty]
      private val bikePoint: BikePoint = new BikePoint("Somewhere", Seq.empty[BikePointAdditionalProperty])

      private val json: String = Json.toJson(bikePoint).toString()
      private val roundTripped: BikePoint = Json.parse(json).as[BikePoint]

      roundTripped.commonName must equalTo("Somewhere")
    }

  }

}
