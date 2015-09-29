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

  val mongoHost: String
  val db: DefaultDB
  val newsitems: BSONCollection
  val tags: BSONCollection

  val all = BSONDocument()

  implicit object TagWriter extends BSONDocumentWriter[Tag] {
    override def write(t: Tag): BSONDocument = {
      BSONDocument(
        "id" -> t.id,
        "name" -> t.name,
        "description" -> t.description,
        "autoTagHints" -> t.autoTagHints
      )
    }
  }

  implicit object NewsitemWriter extends BSONDocumentWriter[Newsitem] {
    override def write(n: Newsitem): BSONDocument = {
      BSONDocument(
        "title" -> n.title,
        "url" -> n.url,
        "date" -> n.date,
        "imageUrl" -> n.imageUrl,
        "body" -> n.body,
        "tags" -> n.tags
      )
    }
  }

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

  def connect(mongoHost: String): DefaultDB = {
    val driver = new MongoDriver
    val connection = driver.connection(List(mongoHost))
    connection("test")
  }

  def writeTag(tag: Tag) = {

    val selector = BSONDocument("id" -> tag.id)
    val update = tags.update(selector, tag, upsert = true)

    update.onComplete {
      case Success(writeResult) =>
        Logger.info("Wrote: " + tag)
      case Failure(e) =>
        Logger.error("Failed to write: " + tag)
    }
  }

  def readTags(): Future[List[Tag]] = {
    tags.find(all).
      cursor[Tag].
      collect[List]()
  }

  def write(newsitem: Newsitem) = {

    val selector = BSONDocument("url" -> newsitem.url)
    val update = newsitems.update(selector, newsitem, upsert = true)

    update.onComplete {
      case Success(writeResult) =>
        Logger.info("Wrote: " + newsitem)
      case Failure(e) =>
        Logger.error("Failed to write: " + newsitem)
    }
  }

  def read(): Future[List[Newsitem]] = {
    newsitems.find(all).
      cursor[Newsitem].
      collect[List]()
  }

  def readByTag(tag: Tag): Future[List[Newsitem]] = {

    val byTag = BSONDocument("tags.id" -> tag.id)

    newsitems.find(byTag).
      cursor[Newsitem].
      collect[List]()
  }

}

object MongoService extends MongoService {

  override lazy val mongoHost: String = Play.configuration.getString("mongo.host").get
  override lazy val db = connect(mongoHost)
  override lazy val newsitems: BSONCollection = db.collection("newsitems")
  override lazy val tags: BSONCollection = db.collection("tags")

}
