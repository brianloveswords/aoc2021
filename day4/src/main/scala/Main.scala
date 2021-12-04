import collections.pivot
import scala.annotation.targetName

/*
@main def main(file: String) =
  val rawLines: Seq[String] = file match
    case "example" => docExample.trim.split("\n").toSeq
    case file      => linesFromFile(file)
 */

enum Cell:
  case Open(v: Draw)
  case Marked(v: Draw)

  def compareAndMark(v: Draw): Cell = this match
    case Open(found) if v == found => Marked(v)
    case _                         => this

  def isMarked: Boolean = this match
    case Marked(_) => true
    case _         => false

object Cell extends (Draw => Cell):
  def apply(v: Draw): Cell = Open(v)
  @targetName("applyInt")
  def apply(v: Int): Cell = Open(Draw(v))

opaque type Draw = Int
object Draw:
  def apply(v: Int): Draw = v

case class Board(data: Seq[Seq[Cell]]):
  override def toString: String =
    (data
      .map { row =>
        val cells = row.map(cell => cell.toString.padTo(10, ' '))
        cells.mkString(" ")
      })
      .mkString("\n")

  def mark(v: Draw): Board =
    Board(data.map(_.map(_.compareAndMark(v))))

  def playUntilWon: Seq[Draw] = ???

  def hasWon =
    data.exists(row => Set(row) == Set(row.filterNot(_.isMarked))) ||
      pivot(data).exists(col => Set(col) == Set(col.filterNot(_.isMarked)))

def parseDraws(line: String): Seq[Int] =
  line.split(",").map(_.toInt)

def parseBoards(lines: Seq[String]): Seq[Board] =
  ???

// @main def main =
//   val rawLines: Seq[String] = file match
//     case "example" => docExample.trim.split("\n").toSeq
//     case file      => linesFromFile(file)

//   println("Hello, world!")
