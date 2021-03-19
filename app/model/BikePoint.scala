package model

case class BikePoint(id: String, commonName: String, additionalProperties: Seq[BikePointAdditionalProperty]) {

  def bikesAvailable = {
    additionalProperties find (ap => {ap.key == "NbBikes"})
  }

  def spacesAvailable = {
    additionalProperties find (ap => {ap.key == "NbEmptyDocks"})
  }

}

case class BikePointAdditionalProperty(key: String, value: String) {}