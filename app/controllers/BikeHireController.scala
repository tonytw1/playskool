package controllers

import play.api.mvc._
import services.{ElasticSearchService, TFLService}
import views.{WithHeader, Header}

import scala.concurrent.ExecutionContext.Implicits.global

object BikeHireController extends Controller with WithHeader {

  val tflService: TFLService = TFLService
  val elasticSearchService = ElasticSearchService

  def dockingStation(id: Int) = Action.async {

    for {
      dockingStation <- tflService.fetchData("BikePoints_" + id)

    } yield {
      Ok(views.html.docking_station(dockingStation))
    }

    //elasticSearchService.fetchData("British Museum, Bloomsbury")
    //tflService.fetchData("BikePoints_" + id).map { data =>
    //  elasticSearchService.upsert(data)
    //  Ok(views.html.docking_station(data))
    //}

  }

}
