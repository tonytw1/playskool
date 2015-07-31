package controllers

import play.api.mvc._
import services.TFLService

import scala.concurrent.ExecutionContext.Implicits.global

object BikeHireController extends Controller {

  val tflService: TFLService = TFLService

  def dockingStation(id: Int) = Action.async {
    tflService.fetchData(id).map { data =>
      Ok(views.html.docking_station(data))
    }
  }

}