package model

import play.api.libs.json.{Json, Format}

case class FeedItem(val title: String, val url: String, imageUrl: Option[String], body: Option[String], tags: Option[Set[Tag]]) // TODO don't want the Option on tags

object FeedItem {
  implicit val formats: Format[FeedItem] = Json.format[FeedItem]
}