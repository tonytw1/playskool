import model.{FeedItem, BikePointAdditionalProperty, BikePoint}
import org.joda.time.DateTime
import org.joda.time.format.{ISODateTimeFormat, DateTimeFormatter}
import org.specs2.mutable.Specification
import play.api.libs.json.Json.JsValueWrapper
import play.api.libs.json._
import play.api.test._

class JsonSpec extends Specification {

  "Play JSON library" should {

    "Round trip bike point domain object" in new WithApplication {

      // TODO can the formats be put on the domain object to prevent duplication?
      implicit val bikePointAdditionalPropertyReads: Format[BikePointAdditionalProperty]= Json.format[BikePointAdditionalProperty]
      implicit val reads: Format[BikePoint] = Json.format[BikePoint]

      private val bikePoint: BikePoint = new BikePoint("BikePoint_999", "Somewhere", Seq.empty[BikePointAdditionalProperty])

      private val json: String = Json.toJson(bikePoint).toString()
      private val roundTripped: BikePoint = Json.parse(json).as[BikePoint]

      roundTripped.id must equalTo("BikePoint_999")
      roundTripped.commonName must equalTo("Somewhere")
    }

    "Be able to parse a list of objects without hair pulling" in new WithApplication {
      implicit val formats: Format[FeedItem] = Json.format[FeedItem]

      val item: FeedItem = FeedItem("Headline", "http://localhost/meh", None, None, Some(DateTime.now().toDate))

      val feedItems: Seq[FeedItem] = Seq(item)

      private val json: String = Json.toJson(feedItems).toString()
      println(json)

      private val roundTripped = Json.parse(json).as[Seq[FeedItem]]
      println(roundTripped)
    }

    "Be able to round trip date times to ISO date format" in new WithApplication {


      implicit val df: Format[DateTime] = new Format[DateTime] {

        private val iso : DateTimeFormatter = ISODateTimeFormat.dateTimeNoMillis();

         override def writes(o: DateTime): JsValue = {
           JsString(iso.print(o))
        }

        override def reads(json: JsValue): JsResult[DateTime] = {
          json match {
            case JsString(s) => JsSuccess(DateTime.now)
            case _ => throw new RuntimeException()
          }
        }
      }

      case class DatedWidget(id: String, date: DateTime)

      val widget = DatedWidget("123", DateTime.now)

      implicit val formats: Format[DatedWidget] = Json.format[DatedWidget]

      val json = Json.toJson(widget)
      val jsonString = json.toString()

      println("As JSON string: " + jsonString)

      private val roundTrippedJson = Json.parse(jsonString)

      val roundTrippedWidget = roundTrippedJson.as[DatedWidget]

      println("Round tripped: " + roundTrippedWidget)
      println("Round tripped date: " + roundTrippedWidget.date)
    }

  }



}
