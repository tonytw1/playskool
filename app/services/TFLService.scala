package services

import model.{BikePoint, BikePointAdditionalProperty}
import play.api.{Logger, Play}
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.WS
import shade.memcached._

import scala.concurrent.ExecutionContext.Implicits.{global => ec}
import scala.concurrent.Future
import scala.concurrent.duration._

trait TFLService extends MemcachedCodecs {

  val memcached: Memcached

  def fetchData(id: String): Future[BikePoint] = {

    Logger.info("Get bike point by id: " + id)

    val cached: Future[Option[BikePoint]] = fetchFromCache(id)

    cached.flatMap {x =>
      if (!x.isEmpty) {
        Logger.info("Cache hit for: " + id)
        Future.successful(x.get)
      } else {
        Logger.info("Cache miss for: " + id)
        cache(fetchFromLive(id))
      }
    }
  }

  def fetchFromCache(bikePointId: String): Future[Option[BikePoint]] = {
    val keyFor: String = cacheKeyFor(bikePointId)
    Logger.info("Fetching from cache: " + bikePointId)
    memcached.get[BikePoint](keyFor)
  }

  private def fetchFromLive(id: String): Future[BikePoint] = {
    val url: String = "https://api.tfl.gov.uk/Place/" + id
    Logger.info("Fetching from url: " + url)
    WS.url(url).get.map {
      response => {
        Logger.debug("HTTP response body: " + response.body)

        implicit val readsBikePointAdditionalProperty: Reads[BikePointAdditionalProperty] = Json.reads[BikePointAdditionalProperty]
        implicit val readsBikePoint: Reads[BikePoint] = Json.reads[BikePoint]

        Json.parse(response.body).as[BikePoint]
      }
    }
  }

  private def cache(response: Future[BikePoint]) = {
    response.map(x => {
      val keyFor: String = cacheKeyFor(x.id)
      Logger.info("Caching " + x.id + " to: " + keyFor)
      memcached.set(keyFor, x, 1.minute)
    }
    )
    response
  }

  private def cacheKeyFor(bikePointId: String): String = {
    "docking-station-" + bikePointId
  }

}

object TFLService extends TFLService {

  override val memcached = Memcached(Configuration(Play.configuration.getString("memcached.host").get + ":11211"), ec)

}

