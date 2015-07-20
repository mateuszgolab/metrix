enablePlugins(ScalaJSPlugin)

name := "metrix"

scalaVersion := "2.11.6"

libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "0.8.1"
libraryDependencies += "com.lihaoyi" %%% "upickle" % "0.3.4"

// concat all jsdeps into a -jsdeps.js
skip in packageJSDependencies := false

jsDependencies += RuntimeDOM
jsDependencies += ProvidedJS / "data-layer-helper.js"

// to run on NodeJS
scalaJSStage in Global := FastOptStage

// this is to automatically build a -launcher.js which instantiates and starts the app
// persistLauncher in Compile := true
// persistLauncher in Test := false

// uTest settings
libraryDependencies += "com.lihaoyi" %%% "utest" % "0.3.1" % "test"
testFrameworks += new TestFramework("utest.runner.Framework")

// activate integration tests
// it:test will run all test files in src/it/*/*.scala
lazy val root = (project in file(".")).
  configs(IntegrationTest).
  settings(Defaults.itSettings: _*)

libraryDependencies ++= {
  val akkaV       = "2.3.11"
  val akkaStreamV = "1.0-RC4"
  val scalaTestV  = "2.2.4" //"3.0.0-M3"
  Seq(
    "com.typesafe.akka" %% "akka-actor"                     % akkaV       % "it",
    "com.typesafe.akka" %% "akka-stream-experimental"       % akkaStreamV % "it",
    "com.typesafe.akka" %% "akka-http-core-experimental"    % akkaStreamV % "it",
    "com.typesafe.akka" %% "akka-http-experimental"         % akkaStreamV % "it",
    "com.typesafe.akka" %% "akka-http-spray-json-experimental" % akkaStreamV % "it",
    "com.typesafe.akka" %% "akka-http-testkit-experimental" % akkaStreamV % "it",
    "org.scalatest"     %% "scalatest"                      % scalaTestV  % "it",
    "org.seleniumhq.selenium" % "selenium-java"             % "2.45.0"    % "it",
    // the following doesn't work with selenium 2.45
    // "com.github.detro.ghostdriver" % "phantomjsdriver" % "1.1.0"
    "com.codeborne"           % "phantomjsdriver"           % "1.2.1"     % "it"

  )
}

Revolver.settings
// make our WebServer visible to revolver
fullClasspath in Revolver.reStart <<= fullClasspath in IntegrationTest
// to be able to serve produced *.js from the WebServer
fullClasspath in Revolver.reStart += baseDirectory.value / "target"

mainClass in Revolver.reStart := Some("metrix.test.WebServer")

// make a Task out of the InputTask reStart by hardcoding an empty arg list
// this is to be able to depend on it for integration testing.
lazy val reStartTask = taskKey[Unit]("start application in the background")

reStartTask := {
  val _ = Revolver.reStart.toTask("").value
  println("App is running.")
}

testOptions in IntegrationTest +=
  Tests.Setup { () => {
                 val restart = reStartTask.value
                 // FIXME: make it block for the duration of the startup
                 Thread.sleep(1000L)
               }}

// in the below not only you need to assign to those useless val
// but you also can't have comments in the body, or it simply doesn't compile
testOptions in IntegrationTest +=
  Tests.Cleanup { () => {
                   val _ = Revolver.reStop.value // FIXME: doesn't work
                 }}