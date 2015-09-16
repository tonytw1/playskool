package services.mongo

import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.{ElasticClient, HitAs, KeywordAnalyzer, RichSearchHit}
import model.BikePoint
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.update.UpdateResponse
import play.api.{Logger, Play}
import play.api.Play.current

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

import reactivemongo.api._
import scala.concurrent.ExecutionContext.Implicits.global

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import play.api.libs.iteratee.Iteratee

import reactivemongo.bson.BSONDocument
import reactivemongo.api.collections.bson.BSONCollection


trait MongoService {


  def connect() {
    // gets an instance of the driver
    // (creates an actor system)
    val driver = new MongoDriver
    val connection = driver.connection(List("ubuntu.local"))

    // Gets a reference to the database "plugin"
    val db = connection("plugin")

    // Gets a reference to the collection "acoll"
    // By default, you get a BSONCollection.
    val collection = db("acoll")
  }

  def listDocs(collection: BSONCollection) = {
    // Select only the documents which field 'firstName' equals 'Jack'
    val query = BSONDocument("firstName" -> "Jack")
    // select only the fields 'lastName' and '_id'
    val filter = BSONDocument(
      "lastName" -> 1,
      "_id" -> 1)

    /* Let's run this query then enumerate the response and print a readable
     * representation of each document in the response */
    collection.
      find(query, filter).
      cursor[BSONDocument].
      enumerate().apply(Iteratee.foreach { doc =>
      println(s"found document: ${BSONDocument pretty doc}")
    })

    // Or, the same with getting a list
    val futureList: Future[List[BSONDocument]] =
      collection.
        find(query, filter).
        cursor[BSONDocument].
        collect[List]()

    futureList.map { list =>
      list.foreach { doc =>
        println(s"found document: ${BSONDocument pretty doc}")
      }
    }
  }

}

object MongoService extends MongoService {

 // override val client = ElasticClient.remote(Play.configuration.getString("mongo.host").get, 9300)

}
