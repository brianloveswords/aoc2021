import files.linesFromFile
import scala.annotation.targetName

case class Line(a: Point, b: Point):
  lazy val coverage: Seq[Point] =
    val field = for
      x <- absRange(a.x, b.x)
      y <- absRange(a.y, b.y)
    yield Point(x, y)
    field.filter(contains(_))

  // we know only 45 degree lines are allowed so we can use toInt at the end
  lazy val slope: Option[Int] =
    val x = (a.y - b.y).toDouble / (a.x - b.x).toDouble
    if x.isInfinite then None else Some(x.toInt)

  lazy val isDiagonal: Boolean = slope.isDefined && slope.get != 0

  def contains(c: Point): Boolean =
    true
      && c.x >= math.min(a.x, b.x)
      && c.x <= math.max(a.x, b.x)
      && c.y >= math.min(a.y, b.y)
      && c.y <= math.max(a.y, b.y)
      && slope.map((c.y - a.y) == _ * (c.x - a.x)).getOrElse(true)

object Line extends ((Point, Point) => Line):
  def parse(s: String): Line =
    val parts = s.split("->").map(_.trim)
    val start = Point.parse(parts(0))
    val end = Point.parse(parts(1))
    Line(start, end)
