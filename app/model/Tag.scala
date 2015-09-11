package model

import play.api.libs.json.{Format, Json}

case class Tag(val name: String)

object Tag {
  implicit val formats: Format[Tag] = Json.format[Tag]
}