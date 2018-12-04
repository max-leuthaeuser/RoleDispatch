val akkaVersion = "2.5.18"
val shapelessVersion = "2.3.3"
val scalatestVersion = "3.0.5"
val chocoVersion = "4.0.9"
val guavaVersion = "27.0.1-jre"
val emfcommonVersion = "2.15.0"
val emfecoreVersion = "2.15.0"
val umlVersion = "3.1.0.v201006071150"

lazy val noPublishSettings =
  Seq(publish := {}, publishLocal := {}, publishArtifact := false)

lazy val root = (project in file(".")).
  settings(
    name := "SCROLLRoot",
    noPublishSettings
  ).
  aggregate(core, tests, examples)

lazy val commonSettings = Seq(
  scalaVersion := "2.12.8",
  version := "1.7",
  mainClass := None,
  resolvers ++= Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
  ),
  libraryDependencies ++= Seq(
    "com.google.guava" % "guava" % guavaVersion,
    "com.typesafe.akka" %% "akka-actor" % akkaVersion,
    "com.chuusai" %% "shapeless" % shapelessVersion,
    "org.choco-solver" % "choco-solver" % chocoVersion,
    "org.scala-lang" % "scala-reflect" % scalaVersion.value,
    "org.eclipse.emf" % "org.eclipse.emf.common" % emfcommonVersion,
    "org.eclipse.emf" % "org.eclipse.emf.ecore" % emfecoreVersion,
    "org.eclipse.uml2" % "org.eclipse.uml2.uml" % umlVersion
  ),
  javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8"),
  scalacOptions ++= Seq(
    "-deprecation",
    "-feature",
    "-language:dynamics",
    "-language:reflectiveCalls",
    "-language:postfixOps",
    "-language:implicitConversions",
    "-unchecked",
    "-target:jvm-1.8"),
  coverageExcludedPackages := "<empty>;scroll\\.benchmarks\\..*;scroll\\.examples\\.currency"
)

lazy val core = project.
  settings(
    commonSettings,
    name := "SCROLL",
    scalacOptions ++= Seq(
      "-Xfatal-warnings",
      "-Xlint",
      "-Xlint:-missing-interpolator",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-inaccessible",
      "-Ywarn-unused",
      "-Ywarn-unused-import",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard",
      "-Xfuture"),
    organization := "com.github.max-leuthaeuser",
    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases" at nexus + "service/local/staging/deploy/maven2")
    },
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    pomExtra :=
      <url>https://github.com/max-leuthaeuser/SCROLL</url>
        <licenses>
          <license>
            <name>LGPL 3.0 license</name>
            <url>http://www.opensource.org/licenses/lgpl-3.0.html</url>
            <distribution>repo</distribution>
          </license>
        </licenses>
        <scm>
          <connection>scm:git:github.com/max-leuthaeuser/SCROLL.git</connection>
          <developerConnection>scm:git:git@github.com:max-leuthaeuser/SCROLL.git</developerConnection>
          <url>github.com/max-leuthaeuser/SCROLL</url>
        </scm>
        <developers>
          <developer>
            <id>max-leuthaeuser</id>
            <name>Max Leuthaeuser</name>
            <url>https://wwwdb.inf.tu-dresden.de/rosi/investigators/doctoral-students/</url>
          </developer>
        </developers>
  )

lazy val examples = project.
  settings(commonSettings).
  dependsOn(core)

lazy val tests = project.
  settings(
    commonSettings,
    testOptions in Test := Seq(Tests.Filter(s => s.endsWith("Suite"))),
    libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % scalatestVersion % "test")
  ).
  dependsOn(core, examples)

lazy val benchmark = project.
  settings(
    commonSettings,
    mainClass in(Jmh, run) := Some("scroll.benchmarks.RunnerApp")
  ).
  enablePlugins(JmhPlugin).
  dependsOn(core)
