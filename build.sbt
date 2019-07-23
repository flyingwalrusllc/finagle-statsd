
name := "finagle-statsd"
organization := "llc.flyingwalrus"

scalaVersion := "2.12.8"
crossScalaVersions := Seq("2.12.8","2.11.12")

val finagleEcosystemVersion = "19.7.0"

scalacOptions := Seq(
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked"
)

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-stats" % finagleEcosystemVersion,
  "llc.flyingwalrus" %% "scala-statsd" % "2.1.16-SNAPSHOT"
) ++ Seq(
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.scalacheck" %% "scalacheck" % "1.14.0"
).map(_ % Test)

// scoverage testing coverage config
coverageMinimum := 80
coverageFailOnMinimum := true

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

addCommandAlias("testCoverageReport",";clean;coverage;test;coverageReport")
