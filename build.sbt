name := "playskool"

version := "1.0"

lazy val `untitled1` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.7"

resolvers += "Spy" at "http://files.couchbase.com/maven2/"
resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"

libraryDependencies ++= Seq( jdbc , cache , ws, "com.bionicspirit" %% "shade" % "1.6.0" )

libraryDependencies += "com.typesafe.play" %% "anorm" % "2.4.0"

libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-core" % "1.7.0"

libraryDependencies ++= Seq("org.reactivemongo" %% "reactivemongo" % "0.11.7")

libraryDependencies += specs2 % Test

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

maintainer in Linux := "Tony McCrae <tony@eelpieconsulting.co.uk>"

packageSummary in Linux := "Test Play project"

packageDescription := "Check Play concepts here"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.2"


enablePlugins(DockerPlugin)

import com.typesafe.sbt.packager.docker._
// dockerExposedPorts := Seq(9000, 9443)


dockerBaseImage := "debian:jessie-backports"
dockerCommands ++= Seq(
  Cmd("USER", "root"),
  ExecCmd("RUN", "apt-get", "update"),
  ExecCmd("RUN", "apt-get", "upgrade", "-y"),
  ExecCmd("RUN", "apt-get", "install", "-y", "openjdk-8-jdk-headless:amd64")
)