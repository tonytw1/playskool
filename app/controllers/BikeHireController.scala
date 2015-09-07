package controllers

import model.BikePoint
import play.api.mvc._
import services.{ElasticSearchService, TFLService}
import views.{WithHeader, Header}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object BikeHireController extends Controller with WithHeader {

  val tflService: TFLService = TFLService
  val elasticSearchService = ElasticSearchService

  def dockingStation(id: Int) = Action.async {

    val dockingStationCall: Future[BikePoint] = tflService.fetchData("BikePoints_" + id)
    val elasticSearchCall: Future[Array[BikePoint]] = elasticSearchService.fetchData("British Museum, Bloomsbury")

    for {
      ds <- dockingStationCall
      es <- elasticSearchCall

    } yield {
      elasticSearchService.upsert(ds)
      Ok(views.html.docking_station(ds, es))
    }


  }

}
