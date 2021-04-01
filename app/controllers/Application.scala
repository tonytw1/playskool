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

    // Represents a value with will resolve in the Future; non blocking etc
    // The actual work may happen in another thread.

    Logger.info("Requesting bike point")
    val eventualBikePoint: Future[BikePoint] = tflService.fetchBikePoint(defaultDockingStation)
    Logger.info("Dropped though to this line immediately")

    eventualBikePoint.map { bikePoint =>
      Logger.info("Bike point is now available")
      Ok(views.html.docking_station(bikePoint))
    }
    
  }

  // Id is past in from routes as a path parameter
  def dockingStation(id: Int) = Action.async {
    for {
      bikePoint <- tflService.fetchBikePoint(id)
    } yield {
      Ok(views.html.docking_station(bikePoint))
    }
  }

}
