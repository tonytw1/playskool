import com.typesafe.sbt.SbtNativePackager._
import NativePackagerKeys._

name := "playskool"

version := "1.0"

lazy val `untitled1` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

resolvers += "Spy" at "http://files.couchbase.com/maven2/"

libraryDependencies ++= Seq( jdbc , anorm , cache , ws, "com.bionicspirit" %% "shade" % "1.6.0" )

libraryDependencies += "com.sksamuel.elastic4s" %% "elastic4s-core" % "1.7.0"

libraryDependencies ++= Seq("org.reactivemongo" %% "reactivemongo" % "0.11.7")

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  

maintainer in Linux := "Tony McCrae <tony@eelpieconsulting.co.uk>"

packageSummary in Linux := "Test Play project"

packageDescription := "Check Play concepts here"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.2"

