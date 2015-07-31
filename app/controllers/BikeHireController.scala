package controllers

import model.BikePoint
import play.api.mvc._
import services.{TFLService, WidgetService}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object BikeHireController extends Controller {

  val tflService: TFLService = TFLService

  def dockingStation(id: Int) = Action.async {
    tflService.fetchData(id).map { data =>
      Ok(views.html.docking_station(data))
    }
  }

}