package model

import play.api.libs.json.{Json, Format}

trait Newsworthy {
  def title: String
  def url: String
  def imageUrl: Option[String]
  def body: Option[String]
}

trait Tagged {
  def tags: Seq[Tag]
}

case class FeedItem(title: String, url: String, imageUrl: Option[String], body: Option[String]) extends Newsworthy

object FeedItem {
  implicit val formats: Format[FeedItem] = Json.format[FeedItem]
}

case class Newsitem(title: String, url: String, imageUrl: Option[String], body: Option[String], tags: Seq[Tag]) extends Newsworthy with Tagged