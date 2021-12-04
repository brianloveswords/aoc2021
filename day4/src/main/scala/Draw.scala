import collections.pivot
import scala.annotation.targetName

opaque type Draw = Int

extension (x: Draw)
  def toInt: Int = x
  def score(board: Board): Int = board.cells.map(_.score).sum * x

object Draw:
  def apply(x: Int): Draw = x
  def apply(xs: Int*): Seq[Draw] = xs
