package model

import java.util.Date

import play.api.libs.json.{Json, Format}

trait Newsworthy {
  def title: String
  def url: String
  def imageUrl: Option[String]
  def body: Option[String]
  def date: Option[Date]
}

trait Tagged {
  def tags: Seq[Tag]
}

case class FeedItem(title: String, url: String, imageUrl: Option[String], body: Option[String], date: Option[Date]) extends Newsworthy

object FeedItem {
  implicit val formats: Format[FeedItem] = Json.format[FeedItem]
}

case class Newsitem(title: String, url: String, imageUrl: Option[String], body: Option[String], date: Option[Date], tags: Seq[Tag]) extends Newsworthy with Tagged