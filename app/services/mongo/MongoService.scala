package services.mongo

import model.Newsitem
import play.api.{Play, Logger}
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocumentReader, BSONDocument}
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Failure
import scala.util.Success
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

trait MongoService {

  val collection: BSONCollection

  def connect(mongoHost: String): BSONCollection = {

    val driver = new MongoDriver
    val connection = driver.connection(List(mongoHost))

    val db: DefaultDB = connection("test")
    db("test")
  }

  def write(newsitem: Newsitem) = {

    val document = BSONDocument(
      "title" -> newsitem.title,
      "url" -> newsitem.url,
      "imageUrl" -> newsitem.imageUrl,
      "body" -> newsitem.body
    )

    val selector = BSONDocument("url" -> newsitem.url)
    val update = collection.update(selector, document, upsert = true)

    update.onComplete {
      case Failure(e) =>
        Logger.error("Failed to write: " + newsitem)
      case Success(writeResult) =>
        Logger.info("Wrote: " + newsitem)
    }
  }

  def listDocs(): Future[List[Newsitem]] = {

    implicit object NewsitemReader extends BSONDocumentReader[Newsitem] {
      override def read(bson: BSONDocument): Newsitem = {
        Newsitem(bson.getAs[String]("title").get,
          bson.getAs[String]("url").get,
          bson.getAs[String]("imageUrl"),
          bson.getAs[String]("body"),
          None,
          Seq())
      }
    }

    val query = BSONDocument()

    collection.find(query).
      cursor[Newsitem].
      collect[List]()
  }

}

object MongoService extends MongoService {

  override val collection = connect(Play.configuration.getString("mongo.host").get)

}
