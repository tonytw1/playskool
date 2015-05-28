package model

case class BikePoint(var commonName: String, var additionalProperties: Seq[BikePointAdditionalProperty]) {

  def bikesAvailable = {
    additionalProperties find (ap => {ap.key == "NbBikes"})
  }

  def spacesAvailable = {
    additionalProperties find (ap => {ap.key == "NbEmptyDocks"})
  }

}

case class BikePointAdditionalProperty(var key: String, var value: String) {}