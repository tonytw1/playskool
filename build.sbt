name := "playskool"

version := "1.0"

lazy val `untitled1` = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.1"

resolvers += "Spy" at "http://files.couchbase.com/maven2/"

libraryDependencies ++= Seq( jdbc , anorm , cache , ws, "com.bionicspirit" %% "shade" % "1.6.0" )

unmanagedResourceDirectories in Test <+=  baseDirectory ( _ /"target/web/public/test" )  
