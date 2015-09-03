package services

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.search.SearchResponse
import play.api.Logger

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
    //var mappings: MappingDefinition = new MappingDefinition() // TODO
    //client.execute {create index "test" mappings {mappings}}
  }

  def deleteIndex(): Unit = {
    client.execute {delete index INDEX}.await
  }

}

object ElasticSearchService extends ElasticSearchService {

  override val client = ElasticClient.remote("localhost", 9300)

}