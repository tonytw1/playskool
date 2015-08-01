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
    val cacheKey: String = "docking-station-" + id

    val cached: Option[BikePoint] = Cache.getAs[BikePoint](cacheKey)
    if (!cached.isEmpty) {
      Future.successful(cached.get)

    } else {
      fetchFromLive(id).map {ds => {
          Cache.set(cacheKey, ds)
          ds
        }
      }
    }
  }

  private def fetchFromLive(id: Int): Future[BikePoint] = {
    WS.url("https://api.tfl.gov.uk/Place/BikePoints_" + id).get.map {
      response => Json.parse(response.body).as[BikePoint]
    }
  }

}

object TFLService extends TFLService