import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

class TestBoard extends ScalaCheckEffectSuite:
  def updateBoard(b: Board, x: Draw): Board = b.updated(x)

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
    val b2 = draws.foldLeft(b1)(updateBoard)
    assertEquals(b2.winningDraw, Some(Draw(7)), s"should have won:\n$b2")
    assertEquals(b2.score, Some(expectedScore))
  }

  test("test winning vertical") {
    val b1 = defaultBoard
    val draws = Draw(1, 33, 4, 9, 7, 12)
    val expectedScore = Seq(2, 5, 8, 3, 6).sum * 7
    val b2 = draws.foldLeft(b1)(updateBoard)
    assertEquals(b2.winningDraw, Some(Draw(7)), s"should have won:\n$b2")
    assertEquals(b2.score, Some(expectedScore))
  }

  test("not a winner") {
    val b1 = defaultBoard
    val draws = Draw(2, 3, 4, 6, 7, 8)
    val b2 = draws.foldLeft(b1)(updateBoard)
    assert(b2.winningDraw.isEmpty, s"should not have won:\n$b2")
  }

class TestUtils extends ScalaCheckEffectSuite:
  test("ok") {
    assert("ok" == "ok")
  }
