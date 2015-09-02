package controllers

import play.api.mvc._
import services.TFLService
import views.{WithHeader, Header}

import scala.concurrent.ExecutionContext.Implicits.global

object BikeHireController extends Controller with WithHeader {

  val tflService: TFLService = TFLService

  def dockingStation(id: Int) = Action.async {

    tflService.fetchData("BikePoints_" + id).map { data =>
      Ok(views.html.docking_station(data))
    }
  }

}
