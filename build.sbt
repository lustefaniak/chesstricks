import com.typesafe.sbt.SbtNativePackager.Universal

organization := "pl.relationsystems.scalac"

name := "chesstricks"

version := "0.1"

scalaVersion := "2.11.2"

scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature", "-Xlint", "-language:postfixOps")

libraryDependencies ++= {
  Seq(
    "org.scalatest" %% "scalatest" % "2.2.2" % "test",
    "org.scalacheck" %% "scalacheck" % "1.11.5" % "test"
  )
}

packageArchetype.java_application

Revolver.settings

instrumentSettings

ScoverageKeys.highlighting := true

ScoverageKeys.excludedPackages in ScoverageCompile := "org.apache.spark.util.collection.*"