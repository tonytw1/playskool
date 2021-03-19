package controllers

import play.api.mvc._
import services.TFLService
import views.WithHeader

import scala.concurrent.ExecutionContext.Implicits.global

object BikeHireController extends Controller with WithHeader {

  val tflService: TFLService = TFLService
  val id = 797 // TODO push to config

  def dockingStation() = Action.async {
    for {
      ds <- tflService.fetchBikePoint(id)
    } yield {
      Ok(views.html.docking_station(ds))
    }
  }

}
