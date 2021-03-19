package controllers

import play.api.Configuration
import play.api.mvc._
import services.TFLService
import views.WithHeader

import javax.inject.Inject
import scala.concurrent.ExecutionContext.Implicits.global

class Application @Inject()(config: Configuration, TFLService: TFLService) extends Controller with WithHeader {

  val tflService: TFLService = TFLService
  val id = config.get[Int]("dockingstation")

  def dockingStation() = Action.async {
    for {
      ds <- tflService.fetchBikePoint(id)
    } yield {
      Ok(views.html.docking_station(ds))
    }
  }

}
