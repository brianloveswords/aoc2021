import files.linesFromFile
import scala.annotation.targetName

case class VentMap(lines: Seq[Line]):
  lazy val ventRatings = lines
    .flatMap(_.coverage)
    .groupBy(identity)
    .mapValues(_.size)
    .toMap

  lazy val dangerPoints = ventRatings
    .filter(_._2 > 1)

  lazy val maxWidth = lines.flatMap(p => Seq(p.a.x, p.b.x)).max
  lazy val maxHeight = lines.flatMap(p => Seq(p.a.y, p.b.y)).max
  override def toString(): String =
    val sb = new StringBuilder()
    for y <- 0 to maxHeight do
      for x <- 0 to maxWidth do
        val point = Point(x, y)
        val count = ventRatings.getOrElse(point, 0)
        sb.append(if count > 0 then count else ".")
      sb.append("\n")
    sb.toString()

object VentMap:
  @targetName("applyRepeated")
  def apply(lines: Line*): VentMap =
    VentMap(lines)
