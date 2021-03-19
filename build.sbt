name := "playskool"

version := "1.0"

lazy val `playskool` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.12.8"

libraryDependencies ++= Seq(ws, "io.monix" %% "shade" % "1.10.0" )
libraryDependencies += specs2 % Test
libraryDependencies += guice
