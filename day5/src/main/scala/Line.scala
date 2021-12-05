import files.linesFromFile
import scala.annotation.targetName

case class Line(start: Point, end: Point):
  lazy val xRange = absRange(start.x, end.x)
  lazy val yRange = absRange(start.y, end.y)

  lazy val coverage: Seq[Point] =
    println(s"Line: $start -> $end")

    val field1 = for
      x <- xRange
      y <- yRange
    yield Point(x, y)

    field1.filter(contains(_))

  def contains(point: Point): Boolean =
    point.x >= start.x && point.x <= end.x &&
      point.y >= start.y && point.y <= end.y

object Line extends ((Point, Point) => Line):
  def parse(s: String): Line =
    val parts = s.split("->").map(_.trim)
    val start = Point.parse(parts(0))
    val end = Point.parse(parts(1))
    Line(start, end)
