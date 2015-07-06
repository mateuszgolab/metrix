enablePlugins(ScalaJSPlugin)

name := "metrix"

scalaVersion := "2.11.6"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.0"

libraryDependencies += "be.doeraene" %%% "scalajs-jquery" % "0.8.0"
jsDependencies += RuntimeDOM
skip in packageJSDependencies := false

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0-M3" % "test"
libraryDependencies += "org.seleniumhq.selenium" % "selenium-java" % "2.35.0" % "test"

jsDependencies += ProvidedJS / "data-layer-helper.js"
scalaJSStage in Global := FastOptStage

// uTest settings
libraryDependencies += "com.lihaoyi" %%% "utest" % "0.3.1" % "test"
testFrameworks += new TestFramework("utest.runner.Framework")

// persistLauncher in Compile := true
// persistLauncher in Test := false