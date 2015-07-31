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

    var data = Cache.getAs[BikePoint](cacheKey)
    if (data.isEmpty) {
      println("Cache miss")
      WS.url("https://api.tfl.gov.uk/Place/BikePoints_" + id).get.map {
        response => {
          Json.parse(response.body).as[BikePoint]
        }
      }.map {
        ds => {
          Cache.set(cacheKey, ds)
          data = Some(ds)
          println("Cached: " + data)
        }
      }
    } else {
      println("Cache hit")
    }
    Future.successful(data.get)
  }

}

object TFLService extends TFLService
