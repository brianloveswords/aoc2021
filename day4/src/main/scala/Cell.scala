import collections.pivot
import scala.annotation.targetName

enum Cell:
  case Open(o: Int)
  case Marked(x: Int)

  def compareAndMark(x: Draw): Cell = this match
    case Open(o) if x.toInt == o => Marked(x.toInt)
    case _                       => this

  def isMarked: Boolean = this match
    case Marked(_) => true
    case _         => false

  def isOpen: Boolean = this match
    case Open(_) => true
    case _       => false

  def score: Int = this match
    case Open(o) => o.toInt
    case _       => 0

object Cell extends (Int => Cell):
  def apply(o: Int): Cell = Open(o)
  object Row:
    def apply(cells: Int*): Seq[Cell] = cells.map(Cell.apply)

  def parse(s: String): Cell = Open(s.toInt)
