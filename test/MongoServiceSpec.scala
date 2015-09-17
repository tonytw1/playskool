import org.specs2.mutable.Specification
import play.api.test._
import services.mongo.MongoService

class MongoServiceSpec extends Specification {

  "Mongo service" should {

    "Can write objects to collection" in new WithApplication {

      val mongoService :MongoService = MongoService

      val collection = mongoService.connect()
      mongoService.write(collection)
      mongoService.listDocs(collection)
    }
  }

}