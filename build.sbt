val scala3Version = "3.1.0"

val v = new {
  val catsCore = "2.7.0"
  val catsEffect = "3.3.0"
  val scalaCheckEffect = "1.0.3"
  val munitCatsEffect = "1.0.6"
}

name := "aoc2021"

inThisBuild(
  List(
    version := "1.0.0",
    scalaVersion := scala3Version,
    scalacOptions ++= Seq("-rewrite", "-indent"),
    libraryDependencies ++= Seq(
      // main dependencies
      "org.typelevel" %% "cats-core" % v.catsCore,
      "org.typelevel" %% "cats-effect" % v.catsEffect
    ),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "munit-cats-effect-3" % v.munitCatsEffect,
      "org.typelevel" %% "scalacheck-effect" % v.scalaCheckEffect,
      "org.typelevel" %% "scalacheck-effect-munit" % v.scalaCheckEffect
    ).map(_ % Test),

    // options
    Global / onChangedBuildSource := ReloadOnSourceChanges
  )
)

lazy val root = project
  .in(file("."))
  .aggregate(day1)

lazy val day1 = project
  .in(file("day1"))
