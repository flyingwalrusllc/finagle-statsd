
name := "finagle-statsd"
organization := "com.crispywalrus"
scalaVersion := "2.12.10"
crossScalaVersions := Seq("2.11.12",scalaVersion.value)

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
  "com.twitter" %% "finagle-stats" % "19.9.0",
  "com.github.gphat" %% "censorinus" % "2.1.3",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0",
  "org.scalatest" %% "scalatest" % "3.0.8" % Test,
  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test
)

// scoverage testing coverage config
coverageMinimum := 80
coverageFailOnMinimum := true

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

addCommandAlias("testCoverageReport",";clean;coverage;test;coverageReport")
