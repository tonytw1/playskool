package services

import play.api.libs.ws.{WSResponse, WS, WSRequestHolder}
import scala.concurrent.ExecutionContext.Implicits.global
import play.api.Play.current

import scala.concurrent.Future

class TFLService {

  def fetchData(): Future[String] = {
    val holder: WSRequestHolder = WS.url("http://api.prod5.live.tfl.gov.uk/Place/BikePoints_195")
    val eventualResponse: Future[WSResponse] = holder.get

    val result: Future[String] = eventualResponse.map {
      response => response.body
    }
    result
  }

}

object TFLService extends TFLService
