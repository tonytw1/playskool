package services

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

trait ElasticSearchService {

  val INDEX: String = "tfl"
  val DOCKING_STATION: String = "dockingstation"

  val client: ElasticClient

  implicit object BikePointHitAs extends HitAs[BikePoint] {
    override def as(hit: RichSearchHit): BikePoint = {
      BikePoint(hit.id, hit.sourceAsMap("name").toString, Seq())
    }
  }

  def all(q: String): Future[Array[BikePoint]] = {
    Logger.info("Querying Elasticsearch for: " + q)

    val resp: Future[SearchResponse] = client.execute {
      search in INDEX / DOCKING_STATION  }
    resp.map(s => {
      val hits: Long = s.getHits.totalHits()
      Logger.info("Found hits: " + hits)
      val points: Array[BikePoint] = s.as[BikePoint]
      Logger.info("Mapped hits to: " + points)
      points
    })
  }

  def upsert(dockingStation: BikePoint): Unit = {
    Logger.info("Indexing: " + dockingStation)
    val eventualResponse: Future[UpdateResponse] = client execute {
      update(dockingStation.id) in INDEX + "/" + DOCKING_STATION docAsUpsert (
        "name" -> dockingStation.commonName
        )
    }
    Logger.info("Done indexing")

    Logger.info("Showing indexing result: ")
    eventualResponse.map(r => {
      Logger.info("Indexing result: " + r.toString)
    })
    Logger.info("Done showing indexing result")
  }

  def createIndex(): Unit = {
    Logger.info("Checking for existing index")
    client.execute {index exists INDEX}.map(e => {

      if (!e.isExists) {
        Logger.info("Creating index (blocking)")
        client.execute {create index INDEX mappings (
          DOCKING_STATION as (
            "id" typed StringType,
            "name" typed StringType analyzer KeywordAnalyzer
            )
          )}.await
        Logger.info("Index created")
      }
    })
  }

  def deleteIndex() = {
    Logger.info("Deleting index (blocking)")  // TODO any way to check if this is required first?
    try {
      client.execute {
        delete index INDEX
      }.await
      Logger.info("Index deleted")

    } catch {
      case e: Exception => {
        Logger.error("Index delete failed", e)
      }
    }
  }

}

object ElasticSearchService extends ElasticSearchService {

  override val client = ElasticClient.remote(Play.configuration.getString("elasticsearch.host").get, 9300)

  {
    deleteIndex()
    createIndex()
  }

}
