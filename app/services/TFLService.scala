package services

import model.{BikePoint, BikePointAdditionalProperty}
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.WS
import play.api.cache.Cache

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TFLService {

  implicit val readsBikePointAdditionalProperty: Reads[BikePointAdditionalProperty] = Json.reads[BikePointAdditionalProperty]
  implicit val readsBikePoint: Reads[BikePoint] = Json.reads[BikePoint]

  def fetchData(id: Int): Future[BikePoint] = {
    val cached: Future[Option[BikePoint]] = Future.successful(Cache.getAs[BikePoint](cacheKeyFor(id)))

    cached.flatMap {x =>
      if (x.isEmpty) {
        fetchFromLiveAndCache(id)
      } else {
        Future.successful(x.get)
      }
    }
  }

  private def fetchFromLiveAndCache(id: Int): Future[BikePoint] = {
    WS.url("https://api.tfl.gov.uk/Place/BikePoints_" + id).get.map {
      response => {
        val bike: BikePoint = Json.parse(response.body).as[BikePoint]
        Cache.set(cacheKeyFor(id), bike)
        bike
      }
    }
  }

  private def cacheKeyFor(id: Int): String = {
    "docking-station-" + id
  }

}

object TFLService extends TFLService