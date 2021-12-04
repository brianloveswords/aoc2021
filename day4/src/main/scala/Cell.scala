import collections.pivot
import scala.annotation.targetName

enum Cell:
  case Open(v: Draw)
  case Marked(v: Draw)

  def compareAndMark(x: Draw): Cell = this match
    case Open(o) if x == o => Marked(x)
    case _                 => this

  def isMarked: Boolean = this match
    case Marked(_) => true
    case _         => false

  def isOpen: Boolean = this match
    case Open(_) => true
    case _       => false

  def score: Int = this match
    case Open(o) => o.toInt
    case _       => 0

object Cell extends (Draw => Cell):
  def apply(v: Draw): Cell = Open(v)
  @targetName("applyInt")
  def apply(v: Int): Cell = Open(Draw(v))
  object Row:
    def apply(cells: Int*): Seq[Cell] = cells.map(Cell.apply)
