
name := "finagle-censorinus"
organization := "com.crispywalrus"
scalaVersion := "2.11.8"
scalacOptions := Seq(
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked",
  "-Ydelambdafy:method",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Ybackend:GenBCode",
  "-target:jvm-1.8",
  "-Yopt:l:classpath"
)

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-stats" % "6.39.0",
  "com.github.gphat" % "censorinus_2.11" % "2.1.3",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.8.0-RC8",
  "org.scalatest" %% "scalatest" % "3.0.0" % Test,
  "org.scalacheck" %% "scalacheck" % "1.13.2" % Test
)

// scoverage testing coverage config
coverageMinimum := 80
coverageFailOnMinimum := true

publishTo := Some("" at "")
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

addCommandAlias("testCoverageReport",";clean;coverage;test;coverageReport")
