
name := "finagle-censorinus"
organization := "com.crispywalrus"

scalaVersion := "2.12.8"

val finagleEcosystemVersion = com.twitter.BuildInfo.version

scalacOptions := Seq(
  "-encoding", "UTF-8",
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked"
)

libraryDependencies ++= Seq(
  "com.twitter" %% "finagle-stats" % finagleEcosystemVersion,
  "com.github.gphat" %% "censorinus" % "2.1.15",
  "org.scala-lang.modules" %% "scala-java8-compat" % "0.9.0"
) ++ Seq(
  "org.scalatest" %% "scalatest" % "3.0.8",
  "org.scalacheck" %% "scalacheck" % "1.13.5"
).map(_ % Test)

// scoverage testing coverage config
coverageMinimum := 80
coverageFailOnMinimum := true

credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

addCommandAlias("testCoverageReport",";clean;coverage;test;coverageReport")
