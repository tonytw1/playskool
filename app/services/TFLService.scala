package services

import model.{BikePoint, BikePointAdditionalProperty}
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.WS

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TFLService {

  implicit val readsBikePointAdditionalProperty: Reads[BikePointAdditionalProperty] = Json.reads[BikePointAdditionalProperty]
  implicit val readsBikePoint: Reads[BikePoint] = Json.reads[BikePoint]

  def fetchData(): Future[BikePoint] = {
    WS.url("http://api.prod5.live.tfl.gov.uk/Place/BikePoints_195").get.map {
      response => {
        Json.parse(response.body).as[BikePoint]
      }
    }
  }

}

object TFLService extends TFLService
