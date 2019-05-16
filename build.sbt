import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val chesstricks = crossProject(JSPlatform, JVMPlatform, NativePlatform)
.crossType(CrossType.Full).settings(
  organization := "pl.relationsystems",
  name := "chesstricks",
  scalaVersion := "2.12.8",
  version := "0.2.0",
  scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature", "-Xlint", "-language:postfixOps"),
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "utest" % "0.6.7" % "test",
    "com.lihaoyi" %% "acyclic" % "0.1.9" % "provided",
    "org.rogach" %%% "scallop" % "3.2.0"    
  ),
  addCompilerPlugin("com.lihaoyi" %% "acyclic" % "0.1.9"),
  testFrameworks += new TestFramework("utest.runner.Framework"),
  autoCompilerPlugins := true,
  Revolver.settings
).jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.7" % "provided"
    ),
    scalaJSStage in Test := FullOptStage,
    scalacOptions ++= (if (isSnapshot.value) Seq.empty else Seq({
      val a = baseDirectory.value.toURI.toString.replaceFirst("[^/]+/?$", "")
      val g = "https://raw.githubusercontent.com/lustefaniak/chesstricks/"
      s"-P:scalajs:mapSourceURI:$a->$g/v${version.value}/"
    })),
    scalaJSUseMainModuleInitializer := true
  ).nativeSettings(
    scalaVersion := "2.11.12",
  )
lazy val js = chesstricks.js

lazy val jvm = chesstricks.jvm

lazy val native = chesstricks.native

cancelable in Global := true