package controllers

import model.BikePoint
import play.api.mvc._
import play.api.{Configuration, Logger}
import services.TFLService
import views.WithHeader

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class Application @Inject()(config: Configuration, TFLService: TFLService) extends Controller with WithHeader {

  private val tflService = TFLService

  private val defaultDockingStation = config.get[Int]("dockingstation")

  def homepage() = Action.async {
    val placeName: String = "Kings Cross" // vals a immutable so you can depend on this value not changing

    Logger.info("Requesting bike points")
    val eventualNearby: Future[Seq[BikePoint]] = tflService.searchBikePoints(placeName)
    // Futures represent a value with will resolve in the Future; non blocking etc
    // The actual work may happen in another thread.

    // We dropped through to this line immediately without waiting for the network call!
    eventualNearby.map { nearby =>
      // After mapping into the Future was can see the result; think if it as a call back?
      Logger.info("Nearby Bike points response is now available")

      val nearbyNames = nearby.map { bikePoint =>
        bikePoint.commonName
      }
      Logger.info("Found nearby bike points: " + nearbyNames.mkString(", "))
      Ok(views.html.homepage(placeName, nearby))
    }
    
  }

  // Id is past in from routes as a path parameter
  def dockingStation(id: String) = Action.async {
    for { // For comprehension
      bikePoint <- tflService.fetchBikePoint(id)
    } yield {
      Ok(views.html.docking_station(bikePoint))
    }
  }

}
