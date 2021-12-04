import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

class TestBoard extends ScalaCheckEffectSuite:
  def updateBoard(b: Board, m: Draw): Board = b.mark(m)

  test("test winning horizontal") {
    val b = Board(
      Seq(
        Seq(1, 2, 3).map(Cell.apply),
        Seq(4, 5, 6).map(Cell.apply),
        Seq(7, 8, 9).map(Cell.apply),
      ),
    )
    val moves = Seq(1, 10, 8, 9, 7).map(Draw.apply)
    val b2 = moves.foldLeft(b)(updateBoard)
    assert(b2.hasWon, s"should have won:\n$b2")
  }

  test("test winning vertical") {
    val b = Board(
      Seq(
        Seq(1, 2, 3).map(Cell.apply),
        Seq(4, 5, 6).map(Cell.apply),
        Seq(7, 8, 9).map(Cell.apply),
      ),
    )
    val moves = Seq(1, 33, 4, 9, 7).map(Draw.apply)
    val b2 = moves.foldLeft(b)(updateBoard)
    assert(b2.hasWon, s"should have won:\n$b2")
  }

  test("not a winner") {
    val b = Board(
      Seq(
        Seq(1, 2, 3).map(Cell.apply),
        Seq(4, 5, 6).map(Cell.apply),
        Seq(7, 8, 9).map(Cell.apply),
      ),
    )
    val moves = Seq(2, 3, 4, 6, 7, 8).map(Draw.apply)
    val b2 = moves.foldLeft(b)(updateBoard)
    assert(!b2.hasWon, s"should have won:\n$b2")
  }
