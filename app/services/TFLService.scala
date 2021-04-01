package services

import model.{BikePoint, BikePointAdditionalProperty}
import play.api.Logger
import play.api.libs.json._
import play.api.libs.ws.WSClient

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

// Basic client for Transport for London's bike hire scheme API
class TFLService @Inject()(ws: WSClient, config: play.api.Configuration) {

  // Configurable API base end point
  // Use the Play Configuration API to access config value
  private val apiUrl = config.get[String]("api.url")

  def fetchBikePoint(id: Int): Future[BikePoint] = {
    Logger.info("Get bike point by id: " + id)
    val bikePointId = "BikePoints_" + id
    fetchPlace(bikePointId)
  }

  private def fetchPlace(bikePointId: String): Future[BikePoint] = {
    val url = apiUrl + "/Place/" + bikePointId
    // Use the Play Logger API to emit logging
    Logger.info("Fetching from url: " + url)
    ws.url(url).get.map {
      response => {
        Logger.info("HTTP response body: " + response.body)
        // Implicit JSON formatters
        // This is weird
        implicit val readsBikePointAdditionalProperty = Json.reads[BikePointAdditionalProperty]
        implicit val readsBikePoint: Reads[BikePoint] = Json.reads[BikePoint]

        val bikePoint = Json.parse(response.body).as[BikePoint]

        Logger.info("Fetched bike point: " + bikePoint)
        bikePoint
      }
    }
  }

}

