
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
  "com.github.gphat" % "censorinus_2.11" % "2.1.3"
) ++ Seq(
  "org.scalatest" %% "scalatest" % "3.0.0",
  "org.scalacheck" %% "scalacheck" % "1.13.2"
).map(_ % Test)

// scoverage testing coverage config
coverageMinimum := 80
coverageFailOnMinimum := true

publishTo := Some("Artifactory Realm" at "https://artifactory.corp.creditkarma.com:8443/artifactory/jarvis-jenkins-releases")
credentials += Credentials(Path.userHome / ".ivy2" / ".credentials")

addCommandAlias("testCoverageReport",";clean;coverage;test;coverageReport")

lazy val tag = taskKey[Unit]("Tag before publish")
tag := {
  "rm -rf .git/refs/tags/*" !;
  "git fetch origin --tags" !;
  val majorVersion = "0"
  val lastTag = {
    s"git tag -l $majorVersion.* --sort -v:refname" #| "head -n 1" !!
  }.trim

  val tagPattern = """\d+\.(\d+)""".r
  val nextTag = {
    if (lastTag == "") s"$majorVersion.1"
    else {
      val tagPattern(minorVersion) = lastTag.toString
      s"$majorVersion.${minorVersion.toInt + 1}"
    }
  }

  "git checkout master" !;
  "git pull origin master" !;
  s"git tag -a $nextTag -m Version:$nextTag" !;
  "git push --tags origin" !;
  "git push origin master" !
}
