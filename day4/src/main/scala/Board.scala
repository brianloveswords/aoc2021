import collections.pivot
import scala.annotation.targetName

case class Board(
    data: Seq[Seq[Cell]],
    winningDraw: Option[Draw] = None,
):
  override def toString: String =
    (data
      .map { row =>
        val cells = row.map(cell => cell.toString.padTo(10, ' '))
        cells.mkString(" ")
      })
      .mkString("\n")

  def updated(x: Draw): Board =
    if winningDraw.isDefined then this
    else
      val newData = data.map(_.map(_.compareAndMark(x)))
      copy(
        data = newData,
        winningDraw = if boardIsWon(newData) then Some(x) else None,
      )

  lazy val isWon: Boolean = winningDraw.isDefined

  lazy val cells: Seq[Cell] = data.flatten
  lazy val score: Option[Int] = winningDraw.map { _.score(this) }

object Board:
  def parse(s: String): Board =
    val lines = s.split("\n").toSeq
    val data = lines.map {
      _.split("\\s+")
        .filterNot(_.isEmpty)
        .toSeq
        .map(Cell.parse)
    }
    Board(data)

private def boardIsWon(data: Seq[Seq[Cell]]) =
  data.exists(row => Set(row) == Set(row.filter(_.isMarked))) ||
    pivot(data).exists(col => Set(col) == Set(col.filter(_.isMarked)))

def parseInput(s: String): (Seq[Draw], Seq[Board]) =
  val sections = s.split("\n\n")
  val draws = sections.head.split(",").map(_.trim).map(Draw.parse)
  val boards = sections.tail.map(Board.parse)
  (draws, boards)
