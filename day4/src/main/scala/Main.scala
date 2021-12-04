import collections.pivot

/*
@main def main(file: String) =
  val rawLines: Seq[String] = file match
    case "example" => docExample.trim.split("\n").toSeq
    case file      => linesFromFile(file)
 */

enum Cell:
  case Open(v: Int)
  case Marked(v: Int)

  def compareAndMark(v: Int): Cell = this match
    case Open(found) if v == found => Marked(v)
    case _                         => this

  def isMarked: Boolean = this match
    case Marked(_) => true
    case _         => false

case class Board(data: Seq[Seq[Cell]]):
  def mark(v: Int): Board =
    Board(data.map(_.map(_.compareAndMark(v))))

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
