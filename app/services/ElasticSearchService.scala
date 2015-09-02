package services

import com.sksamuel.elastic4s.ElasticClient
import com.sksamuel.elastic4s.ElasticDsl._
import org.elasticsearch.action.search.SearchResponse
import play.api.Logger

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future

class ElasticSearchService {

  def fetchData(): Unit = {
    Logger.info("Querying Elasticsearch")
    val client = ElasticClient.remote("localhost", 9300)

    val resp: Future[SearchResponse] = client.execute {search in "osm20150815" / "places" query "meh"}
    resp.map(s => {
      Logger.info("Hits" + s.getHits.totalHits())
    })
  }

}

object ElasticSearchService extends ElasticSearchService

