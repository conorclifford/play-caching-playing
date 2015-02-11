name := "play-caching"

version := "1.0-SNAPSHOT"

val root = (project in file(".")).enablePlugins(PlayScala).settings(
)

libraryDependencies ++= Seq(
    cache,
    "com.github.mumoshu" %% "play2-memcached" % "0.6.0"
)

scalacOptions += "-feature"

