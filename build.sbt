organization := "pl.relationsystems.scalac"

name := "chesstricks"

version := "0.1"

scalaVersion := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature", "-Xlint", "-language:postfixOps")

libraryDependencies ++= {
  Seq(
    "org.scalatest" %% "scalatest" % "2.2.2" % "test",
    "org.scalamock" %% "scalamock-scalatest-support" % "3.1.4" % "test"
  )
}

libraryDependencies += "org.rogach" %% "scallop" % "0.9.5"

resolvers += Resolver.sonatypeRepo("releases")

packageArchetype.java_application

Revolver.settings

instrumentSettings

ScoverageKeys.highlighting := true

wartremoverErrors ++= Warts.all