package services.mongo

import java.util.Date

import model.{Newsitem, Tag}
import play.api.Play.current
import play.api.{Logger, Play}
import reactivemongo.api._
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.bson.{BSONDocument, BSONDocumentReader, BSONDocumentWriter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

trait MongoService {

  val collection: BSONCollection

  def connect(mongoHost: String): BSONCollection = {

    val driver = new MongoDriver
    val connection = driver.connection(List(mongoHost))

    val db: DefaultDB = connection("test")
    db("test")
  }

  def write(newsitem: Newsitem) = {

    implicit object TagWriter extends BSONDocumentWriter[Tag] {
      override def write(t: Tag): BSONDocument = {
        BSONDocument(
          "id" -> t.id,
          "name" -> t.name
        )
      }
    }

    val document = BSONDocument(
      "title" -> newsitem.title,
      "url" -> newsitem.url,
      "date" -> newsitem.date,
      "imageUrl" -> newsitem.imageUrl,
      "body" -> newsitem.body,
      "tags" -> newsitem.tags
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

    implicit object TagReader extends BSONDocumentReader[Tag] {
      override def read(bson: BSONDocument): Tag = {
        Tag(bson.getAs[Int]("id").get,
          bson.getAs[String]("name").get,
          None,
          None
          )
      }
    }

    implicit object NewsitemReader extends BSONDocumentReader[Newsitem] {
      override def read(bson: BSONDocument): Newsitem = {
        Newsitem(bson.getAs[String]("title").get,
          bson.getAs[String]("url").get,
          bson.getAs[String]("imageUrl"),
          bson.getAs[String]("body"),
          bson.getAs[Date]("date"),
          bson.getAs[Seq[Tag]]("tags").get)
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
