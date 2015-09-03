package services

import com.sksamuel.elastic4s.{KeywordAnalyzer, ElasticClient, SimpleAnalyzer}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType.GeoPointType
import model.BikePoint
import org.elasticsearch.action.search.SearchResponse
import org.elasticsearch.action.update.UpdateResponse
import play.api.Logger
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.ElasticDsl._

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait ElasticSearchService {

  val INDEX: String = "tfl"
  val DOCKING_STATION: String = "dockingstation"

  def client: ElasticClient

  def fetchData(q: String): Unit = {
    Logger.info("Querying Elasticsearch for: " + q)

    val resp: Future[SearchResponse] = client.execute {
      search in INDEX / DOCKING_STATION query { term("name", q)} limit 10}
    resp.map(s => {
      Logger.info("Hits" + s.getHits.totalHits())
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
    Logger.info("Creating index (blocking)")
    client.execute {create index INDEX mappings (
      DOCKING_STATION as (
        "id" typed StringType,
        "name" typed StringType analyzer KeywordAnalyzer
        )
      )}.await

    Logger.info("Index created")
  }

  def deleteIndex(): Unit = { // TODO needs to fail gracefully if index doesn't exist
    Logger.info("Deleting index (blocking)")
    client.execute {delete index INDEX}.await
    Logger.info("Index deleted")
  }

}

object ElasticSearchService extends ElasticSearchService {

  override val client = ElasticClient.remote("ubuntu.local", 9300)

  {
    deleteIndex()
    createIndex()
  }

}