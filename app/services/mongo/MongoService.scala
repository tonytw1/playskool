package services.mongo

import play.api.Logger
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.BSONDocument
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Failure
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global

trait MongoService {

  def connect(): BSONCollection = {

    val driver = new MongoDriver
    val connection = driver.connection(List("localhost"))

    val db: DefaultDB = connection("test")


    val collection: BSONCollection = db("test")
    Logger.info("Collection: " + collection.name)
    collection
  }

  def write(collection: BSONCollection) = {
    Logger.info("Writing")
    val document = BSONDocument(
      "firstName" -> "Stephane",
      "lastName" -> "Godbillon"
    )

    val insert = collection.insert(document)

    insert.onComplete {
      case Failure(e) =>
        Logger.error("FAIL")
      case Success(writeResult) =>
        Logger.info("DONE")
      case _ =>
        Logger.info("MEH")
    }

    Logger.info("Finished writing")
  }

  def listDocs(collection: BSONCollection) = {
    val query = BSONDocument()

    Logger.info("Results")

    val futureList: Future[List[BSONDocument]] =
      collection.find(query).
        cursor[BSONDocument].
        collect[List]()

    val map = futureList.map { list =>
      Logger.info("List: " + list.size)
      list.foreach { doc =>
        Logger.info(s"found document: ${BSONDocument pretty doc}")
      }
    }

    Await.ready(map, Duration.Inf)

    Logger.info("End")
  }

}

object MongoService extends MongoService {

 // override val client = ElasticClient.remote(Play.configuration.getString("mongo.host").get, 9300)

}
