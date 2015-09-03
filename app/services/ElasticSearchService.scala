package services

import com.sksamuel.elastic4s.{KeywordAnalyzer, ElasticClient, SimpleAnalyzer}
import com.sksamuel.elastic4s.ElasticDsl._
import com.sksamuel.elastic4s.mappings.FieldType.GeoPointType
import org.elasticsearch.action.search.SearchResponse
import play.api.Logger
import com.sksamuel.elastic4s.mappings.FieldType._
import com.sksamuel.elastic4s.ElasticDsl._

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

trait ElasticSearchService {

  val INDEX: String = "test"

  def client: ElasticClient

  def fetchData(): Unit = {
    Logger.info("Querying Elasticsearch")

    val resp: Future[SearchResponse] = client.execute {search in INDEX / "places" query "meh" limit 10}
    resp.map(s => {
      Logger.info("Hits" + s.getHits.totalHits())
    })
  }


  def createIndex(): Unit = {
    client.execute {create index "test" mappings (

      "city" as (
        "year_founded" typed IntegerType,
        "location" typed GeoPointType,
        "demonym" typed StringType nullValue "citizen" analyzer KeywordAnalyzer
        ),
      "country" as (
        "name" typed StringType analyzer SimpleAnalyzer
        ) dateDetection true dynamicDateFormats("dd/MM/yyyy", "dd-MM-yyyy")

      )}.await
  }

  def deleteIndex(): Unit = {
    client.execute {delete index INDEX}.await
  }

}

object ElasticSearchService extends ElasticSearchService {

  override val client = ElasticClient.remote("localhost", 9300)

}