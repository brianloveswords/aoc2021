import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen
import cats.syntax.option.*

class TestBoard extends ScalaCheckEffectSuite:
  def defaultBoard = Board(
    Seq(
      Cell.Row(1, 2, 3),
      Cell.Row(4, 5, 6),
      Cell.Row(7, 8, 9),
    ),
  )

  test("test winning horizontal") {
    val b1 = defaultBoard
    val draws = Draw(1, 10, 8, 9, 7)
    val expectedScore = Seq(2, 3, 4, 5, 6).sum * 7
    val b2 = draws.foldLeft(b1)(_ updated _)
    assertEquals(b2.winningDraw, Some(Draw(7)), s"should have won:\n$b2")
    assertEquals(b2.score, Some(expectedScore))
  }

  test("test winning vertical") {
    val b1 = defaultBoard
    val draws = Draw(1, 33, 4, 9, 7, 12)
    val expectedScore = Seq(2, 5, 8, 3, 6).sum * 7
    val b2 = draws.foldLeft(b1)(_ updated _)
    assertEquals(b2.winningDraw, Some(Draw(7)), s"should have won:\n$b2")
    assertEquals(b2.score, Some(expectedScore))
  }

  test("not a winner") {
    val b1 = defaultBoard
    val draws = Draw(2, 3, 4, 6, 7, 8)
    val b2 = draws.foldLeft(b1)(_ updated _)
    assert(b2.winningDraw.isEmpty, s"should not have won:\n$b2")
  }

  test("parse board") {
    val boardString = """
      |1 2 3
      |4 5 6
      |7 8 9
    """.trim.stripMargin
    val board = Board.parse(boardString)
    assertEquals(board, defaultBoard)
  }

class TestUtils extends ScalaCheckEffectSuite:
  test("parseInput") {
    val (draws, boards) = parseInput(
      """
        |1,2,3
        |
        |1 2 3
        |4 5 6
        |7 8 9
        |
        |9 8 7
        |6 5 4
        |3 2 1
        |
        |6 5 4
        |9 8 7
        |3 2 1
      """.trim.stripMargin,
    )

    assertEquals(draws, Draw(1, 2, 3))
    assertEquals(boards.size, 3)
    assertEquals(
      boards(0),
      Board(
        Seq(
          Cell.Row(1, 2, 3),
          Cell.Row(4, 5, 6),
          Cell.Row(7, 8, 9),
        ),
      ),
    )
    assertEquals(
      boards(1),
      Board(
        Seq(
          Cell.Row(9, 8, 7),
          Cell.Row(6, 5, 4),
          Cell.Row(3, 2, 1),
        ),
      ),
    )
    assertEquals(
      boards(2),
      Board(
        Seq(
          Cell.Row(6, 5, 4),
          Cell.Row(9, 8, 7),
          Cell.Row(3, 2, 1),
        ),
      ),
    )
  }

  test("example") {
    val (draws, boards) = parseInput(docExample)

    val winData = draws.foldLeft((Seq.empty[Board], boards)) { (acc, draw) =>
      acc match
        case (Seq(), boards) =>
          val updatedBoards = boards.map(_.updated(draw))
          val winningSet = updatedBoards.filter(_.winningDraw.isDefined).toSeq
          (winningSet, updatedBoards)
        case otherwise => otherwise
    }

    println(winData._1.map(_.score))

    // println(draws)
    // println(winner.get)
    // println(b2.mkString("\n\n"))

  }
