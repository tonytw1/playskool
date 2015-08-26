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

  implicit val readsBikePointAdditionalProperty: Reads[BikePointAdditionalProperty] = Json.reads[BikePointAdditionalProperty]
  implicit val readsBikePoint: Reads[BikePoint] = Json.reads[BikePoint]

  def fetchData(id: String): Future[BikePoint] = {

    Logger.info("Get bike point by id: " + id)

    val cached: Future[Option[BikePoint]] = Future.successful(Cache.getAs[BikePoint](cacheKeyFor(id)))

    cached.flatMap {x =>
      if (x.isEmpty) {
        Logger.info("Cache miss for: " + id)
        cache(fetchFromLive(id))
      } else {
        Logger.info("Cache hit for: " + id)
        Future.successful(x.get)
      }
    }
  }

  private def fetchFromLive(id: String): Future[BikePoint] = {
    val url: String = "https://api.tfl.gov.uk/Place/" + id
    Logger.info("Fetching from url: " + url)
    WS.url(url).get.map {
      response => {
        Logger.debug("HTTP response body: " + response.body)
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

  private def cacheKeyFor(id: String): String = {
    "docking-station-" + id
  }

}

object TFLService extends TFLService