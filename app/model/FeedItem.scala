package model

case class FeedItem(val title: String, val url: String, imageUrl: Option[String], body: Option[String])