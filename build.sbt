name := """PlayCRUD"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.0"

//addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.5.10")

libraryDependencies ++= Seq(
  // Play Dependencies
//  jdbc,
//  ehcache,
//  ws,
  guice,

  // DB connector
  "mysql" % "mysql-connector-java" % "8.0.16",

  // Slick
//  "com.typesafe.slick" %% "slick" % "|release|",
//  "org.slf4j" % "slf4j-nop" % "1.6.4"
  "com.typesafe.play" %% "play-slick-evolutions" % "4.0.2",
  "com.typesafe.play" %% "play-slick" % "4.0.2",

  routes := InjectedRoutesGenerator

)

libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "4.0.3" % Test
//libraryDependencies += "com.typesafe.slick" %% "slick-codegen" % "3.2.1"

scalacOptions ++= Seq(
  "-feature",
  "-deprecation",
  "-Xfatal-warnings"
)