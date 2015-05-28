package services

import play.api.Play.current
import play.api.libs.json._
import play.api.libs.ws.{WS, WSRequestHolder, WSResponse}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class TFLService {

  def fetchData(): Future[JsValue] = {
    val holder: WSRequestHolder = WS.url("http://api.prod5.live.tfl.gov.uk/Place/BikePoints_195")
    val eventualResponse: Future[WSResponse] = holder.get

    val result: Future[JsValue] = eventualResponse.map {
      response => response.json
    }
    result
  }

}

object TFLService extends TFLService
