name := "play-caching"

version := "1.0-SNAPSHOT"

scalaVersion := "2.11.4"

val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies ++= Seq(
    cache,
    "com.typesafe.play.plugins" %% "play-plugins-redis" % "2.3.1"
)

resolvers ++= Seq(
    "pk11 repo" at "http://pk11-scratch.googlecode.com/svn/trunk",
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/"
)

scalacOptions += "-feature"

