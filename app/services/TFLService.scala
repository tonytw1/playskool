package services

import model.{BikePoint, BikePointAdditionalProperty}
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.cache.Cache

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import play.api.Logger

class TFLService {

  def fetchData(id: String): Future[BikePoint] = {

    Logger.info("Get bike point by id: " + id)
    val bikePointId = "BikePoints_" + id

    val cached: Future[Option[BikePoint]] = fetchFromCache(bikePointId)

    cached.flatMap {x =>
      if (!x.isEmpty) {
        Logger.info("Cache hit for: " + bikePointId)
        Future.successful(x.get)
      } else {
        Logger.info("Cache miss for: " + bikePointId)
        cache(fetchFromLive(id))
      }
    }
  }

  def fetchFromCache(bikePointId: Nothing): Future[Option[BikePoint]] = {
    Future.successful(Cache.getAs[BikePoint](cacheKeyFor(bikePointId)))
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
    response.map(x =>
      Cache.set(cacheKeyFor(x.id), x)
    )
    response
  }

  private def cacheKeyFor(bikePointId: String): String = {
    "docking-station-" + bikePointId
  }

}

object TFLService extends TFLService