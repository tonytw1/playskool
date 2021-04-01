name := "playskool"

version := "1.0"

lazy val `playskool` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies += ws
libraryDependencies += guice

libraryDependencies += specs2 % Test
