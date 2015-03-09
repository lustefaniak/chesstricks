
val chesstricks = crossProject.settings(
  organization := "pl.relationsystems",
  name := "chesstricks",
  scalaVersion := "2.11.5",
  version := "0.2.0",
  scalacOptions := Seq("-unchecked", "-deprecation", "-encoding", "utf8", "-feature", "-Xlint", "-language:postfixOps"),
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "utest" % "0.3.1" % "test",
    "com.lihaoyi" %% "acyclic" % "0.1.2" % "provided"
  ),
  addCompilerPlugin("com.lihaoyi" %% "acyclic" % "0.1.2"),
  testFrameworks += new TestFramework("utest.runner.Framework"),
  autoCompilerPlugins := true,
  wartremoverErrors ++= Warts.all
).jsSettings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.7.0" % "provided"
    ),
    scalaJSStage in Test := FullOptStage,
    scalacOptions ++= (if (isSnapshot.value) Seq.empty else Seq({
      val a = baseDirectory.value.toURI.toString.replaceFirst("[^/]+/?$", "")
      val g = "https://raw.githubusercontent.com/lustefaniak/chesstricks/"
      s"-P:scalajs:mapSourceURI:$a->$g/v${version.value}/"
    }))
  ).jvmSettings(Seq(
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.2" % "test",
      "org.scalamock" %% "scalamock-scalatest-support" % "3.1.4" % "test",
      "org.rogach" %% "scallop" % "0.9.5"
    )) ++ Revolver.settings :_*
  )
lazy val js = chesstricks.js

lazy val jvm = chesstricks.jvm
