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
      "org.typelevel" %% "cats-effect" % v.catsEffect,
    ),
    libraryDependencies ++= Seq(
      "org.typelevel" %% "munit-cats-effect-3" % v.munitCatsEffect,
      "org.typelevel" %% "scalacheck-effect" % v.scalaCheckEffect,
      "org.typelevel" %% "scalacheck-effect-munit" % v.scalaCheckEffect,
    ).map(_ % Test),

    // options
    Global / onChangedBuildSource := ReloadOnSourceChanges,
  ),
)

lazy val root = project
  .in(file("."))
  .aggregate(core, day1, day2, day3, day4, day5, day6, day7, day8)

lazy val core = project
  .in(file("core"))

lazy val day1 = project
  .in(file("day1"))
  .dependsOn(core)

lazy val day2 = project
  .in(file("day2"))
  .dependsOn(core)

lazy val day3 = project
  .in(file("day3"))
  .dependsOn(core)

lazy val day4 = project
  .in(file("day4"))
  .dependsOn(core)

lazy val day5 = project
  .in(file("day5"))
  .dependsOn(core)

lazy val day6 = project
  .in(file("day6"))
  .dependsOn(core)

lazy val day7 = project
  .in(file("day7"))
  .dependsOn(core)

lazy val day8 = project
  .in(file("day8"))
  .dependsOn(core)
