package services

import model.BikePoint
import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.{WS, WSRequestHolder, WSResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TFLService {

  def fetchData(): Future[BikePoint] = {
    val holder: WSRequestHolder = WS.url("http://api.prod5.live.tfl.gov.uk/Place/BikePoints_195")
    val eventualResponse: Future[WSResponse] = holder.get

    val result: Future[BikePoint] = eventualResponse.map {
      response => {
        val json: String = response.body
        val bikePoint: BikePoint = Json.parse(json).as[BikePoint]
        bikePoint
      }

    }
    result
  }

}

object TFLService extends TFLService
