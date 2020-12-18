val lib = Dependencies

val utf8 = java.nio.charset.StandardCharsets.UTF_8.toString

Global / onChangedBuildSource := ReloadOnSourceChanges

// enables experimental Turbo mode with ClassLoader layering from sbt 1.3
ThisBuild / turbo := true

ThisBuild / scalaVersion := lib.v.scalaVersion

lazy val noPublishSettings =
  Seq(publish := {}, publishLocal := {}, publishArtifact := false)

lazy val root = (project in file(".")).
  settings(
    name := "SCROLLRoot",
    noPublishSettings
  ).
  aggregate(core, tests, examples)

lazy val commonSettings = Seq(
  version := "3.0",
  resolvers ++= Seq(
    "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    "Sonatype OSS Releases" at "https://oss.sonatype.org/content/repositories/releases"
  ),
  libraryDependencies ++= lib.coreDependencies,
  dependencyOverrides ++= lib.coreDependenciesOverrides,
  javacOptions in Compile ++= Seq("-source", "1.8", "-target", "1.8"),
  scalacOptions ++= Seq(
    "-encoding", utf8,
    "-deprecation",                       // Emit warning and location for usages of deprecated APIs.
    "-feature",                           // Emit warning and location for usages of features that should be imported explicitly.
    "-language:dynamics",                 // Allow direct or indirect subclasses of scala.Dynamic.
    "-language:reflectiveCalls",          // Allow reflective access to members of structural types.
    "-language:postfixOps",               // Allow postfix operator notation.
    "-language:implicitConversions",      // Allow definition of implicit functions called views.
    "-unchecked",                         // Enable additional warnings where generated code depends on assumptions.
    "-target:jvm-" + lib.v.jvm),
  coverageExcludedPackages := "<empty>;scroll\\.benchmarks\\..*",
  updateOptions := updateOptions.value.withCachedResolution(true),
  historyPath := Option(target.in(LocalRootProject).value / ".history"),
  cancelable in Global := true,
  logLevel in Global := {
    if (insideCI.value) Level.Error else Level.Info
  },
  initialize ~= { _ =>
    val ansi = System.getProperty("sbt.log.noformat", "false") != "true"
    if (ansi) System.setProperty("scala.color", "true")
  }
)

lazy val core = project.
  settings(
    commonSettings,
    mainClass in (Compile, run) := None,
    name := "SCROLL",
    scalacOptions += "-Xfatal-warnings",
    organization := "com.github.max-leuthaeuser",
    publishTo := sonatypePublishToBundle.value,
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
    fork in Test := true,
    testOptions in Test := Seq(
      Tests.Argument(TestFrameworks.ScalaTest
        // F: show full stack traces
        // S: show short stack traces
        // D: show duration for each test
        // I: print "reminders" of failed and canceled tests at the end of the summary,
        //    eliminating the need to scroll and search to find failed or canceled tests.
        //    replace with G (or T) to show reminders with full (or short) stack traces
        // K: exclude canceled tests from reminder
        , "-oDI"
        // Periodic notification of slowpokes (tests that have been running longer than 30s)
        , "-W", "30", "30"
      )
    ), libraryDependencies ++= lib.testDependencies
  ).
  dependsOn(core, examples)

lazy val benchmark = project.
  settings(
    commonSettings,
    mainClass in(Jmh, run) := Option("scroll.benchmarks.RunnerApp")
  ).
  enablePlugins(JmhPlugin).
  dependsOn(core)
