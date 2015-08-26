import model.{BikePointAdditionalProperty, BikePoint}
import org.specs2.mutable.Specification
import play.api.libs.json._
import play.api.test._

class JsonSpec extends Specification {

  "Play JSON library" should {

    "Round trip bike point domain object" in new WithApplication {
      implicit val bikePointAdditionalPropertyReads: Format[BikePointAdditionalProperty]= Json.format[BikePointAdditionalProperty]
      implicit val reads: Format[BikePoint] = Json.format[BikePoint]
      private val bikePoint: BikePoint = new BikePoint("BikePoint_999", "Somewhere", Seq.empty[BikePointAdditionalProperty])

      private val json: String = Json.toJson(bikePoint).toString()
      private val roundTripped: BikePoint = Json.parse(json).as[BikePoint]

      roundTripped.id must equalTo("BikePoint_999")
      roundTripped.commonName must equalTo("Somewhere")
    }

  }

}
