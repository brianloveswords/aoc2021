import files.linesFromFile
import scala.annotation.targetName

case class Point(x: Int, y: Int):
  override def toString: String = s"($x, $y)"
  def translate(x: Int, y: Int): Point =
    Point(this.x + x, this.y + y)

object Point extends ((Int, Int) => Point):
  def parse(s: String): Point =
    val xs = s.split(",").map(_.trim)
    Point(xs(0).toInt, xs(1).toInt)