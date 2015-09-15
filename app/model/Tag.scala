package model

import play.api.libs.json.{Format, Json}

case class Tag(val id: Int, val name: String, val autoTagHints: Option[String])

object Tag {
  implicit val formats: Format[Tag] = Json.format[Tag]
}