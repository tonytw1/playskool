package services

import model.{BikePoint, BikePointAdditionalProperty}
import play.api.libs.json._
import play.api.libs.ws.WSClient
import play.api.Logger
import shade.memcached.{Configuration, Memcached, MemcachedCodecs}

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

class TFLService @Inject()(ws: WSClient, config: play.api.Configuration) extends MemcachedCodecs {

  private val memcachedLocation = config.getString("memcached.location").get
  val memcached = Memcached(Configuration(memcachedLocation))


  def fetchBikePoint(id: Int): Future[BikePoint] = {
    Logger.info("Get bike point by id: " + id)
    val bikePointId = "BikePoints_" + id

    fetchFromCache(bikePointId).flatMap { cacheHit =>
      if (cacheHit.isDefined) {
        Logger.info("Cache hit for: " + id)
        Future.successful(cacheHit.get)
      } else {
        Logger.info("Cache miss for: " + id)
        cache(fetchFromLive(bikePointId))
      }
    }
  }

  def fetchFromCache(bikePointId: String): Future[Option[BikePoint]] = {
    val keyFor = cacheKeyFor(bikePointId)
    Logger.info("Fetching from cache: " + bikePointId)
    memcached.get[BikePoint](keyFor)
  }

  private def fetchFromLive(bikePointId: String): Future[BikePoint] = {
    val url = "https://api.tfl.gov.uk/Place/" + bikePointId
    Logger.info("Fetching from url: " + url)
    ws.url(url).get.map {
      response => {
        Logger.info("HTTP response body: " + response.body)
        implicit val readsBikePointAdditionalProperty = Json.reads[BikePointAdditionalProperty]
        implicit val readsBikePoint: Reads[BikePoint] = Json.reads[BikePoint]
        Json.parse(response.body).as[BikePoint]
      }
    }
  }

  private def cache(response: Future[BikePoint]): Future[BikePoint] = {
    response.map(x => {
      val keyFor: String = cacheKeyFor(x.id)
      Logger.info("Caching " + x.id + " to: " + keyFor)
      memcached.set(keyFor, x, 10.second)
    }
    )
    response
  }

  private def cacheKeyFor(bikePointId: String): String = "docking-station-" + bikePointId

}

