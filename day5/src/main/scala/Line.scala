import files.linesFromFile
import scala.annotation.targetName

case class Line(start: Point, end: Point):
  lazy val coverage: Seq[Point] =
    val field = for
      x <- absRange(start.x, end.x)
      y <- absRange(start.y, end.y)
    yield Point(x, y)
    field.filter(contains(_))

  // we know only 45 degree lines are allowed so we can use toInt at the end
  lazy val slope: Option[Int] =
    val x = (start.y - end.y).toDouble / (start.x - end.x).toDouble
    if x.isInfinite then None else Some(x.toInt)

  lazy val isDiagonal: Boolean = slope.isDefined && slope.get != 0

  def contains(point: Point): Boolean = slope match
    case Some(slope) =>
      val a = start
      val b = end
      val c = point

      val on = (c.y - a.y) == slope * (c.x - a.x)
      val between = (math.min(a.x, b.x) <= c.x) && (c.x <= math.max(a.x, b.x))

      on && between

    case None =>
      point.x >= math.min(start.x, end.x) && point.x <= math.max(start.x, end.x)

object Line extends ((Point, Point) => Line):
  def parse(s: String): Line =
    val parts = s.split("->").map(_.trim)
    val start = Point.parse(parts(0))
    val end = Point.parse(parts(1))
    Line(start, end)
