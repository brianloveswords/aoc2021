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
        winningDraw = if isWon(newData) then Some(x) else None,
      )

  lazy val cells: Seq[Cell] = data.flatten
  lazy val score: Option[Int] = winningDraw.map { _.score(this) }

def isWon(data: Seq[Seq[Cell]]) =
  data.exists(row => Set(row) == Set(row.filter(_.isMarked))) ||
    pivot(data).exists(col => Set(col) == Set(col.filter(_.isMarked)))
