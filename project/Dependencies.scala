import sbt.stringToOrganization

object Dependencies extends Dependencies

trait Dependencies {

  object v {
    val scalaVersion = "2.13.0"
    val akkaVersion = "2.5.23"
    val shapelessVersion = "2.3.3"
    val scalatestVersion = "3.1.0-SNAP13"
    val chocoVersion = "4.10.1"
    val guavaVersion = "28.1-jre"
    val emfcommonVersion = "2.15.0"
    val emfecoreVersion = "2.15.0"
    val umlVersion = "3.1.0.v201006071150"
    // TODO: re-enable after a version for Scala 2.13.0 is available
    // val linterVersion = "0.1.17"
    val jvm = "1.8"
  }

  val coreDependencies = Seq(
    "com.google.guava" % "guava" % v.guavaVersion,
    "com.typesafe.akka" %% "akka-actor" % v.akkaVersion,
    "com.chuusai" %% "shapeless" % v.shapelessVersion,
    "org.choco-solver" % "choco-solver" % v.chocoVersion,
    "org.scala-lang" % "scala-reflect" % v.scalaVersion,
    "org.eclipse.emf" % "org.eclipse.emf.common" % v.emfcommonVersion,
    "org.eclipse.emf" % "org.eclipse.emf.ecore" % v.emfecoreVersion,
    "org.eclipse.uml2" % "org.eclipse.uml2.uml" % v.umlVersion
  )

  val coreDependenciesOverrides = Seq(
    "org.scala-lang" % "scala-library" % v.scalaVersion,
    "org.eclipse.emf" % "org.eclipse.emf.common" % v.emfcommonVersion,
    "org.eclipse.emf" % "org.eclipse.emf.ecore" % v.emfecoreVersion
  )

  val testDependencies = Seq(
    "org.scalatest" %% "scalatest" % v.scalatestVersion % "test"
  )

  // TODO: re-enable after a version for Scala 2.13.0 is available
  // val linter = "org.psywerx.hairyfotr" %% "linter" % v.linterVersion
}
