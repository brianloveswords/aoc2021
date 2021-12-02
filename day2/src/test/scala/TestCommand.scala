import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

class TestCommand extends ScalaCheckEffectSuite:
  test("parse examples") {
    val examples = List(
      "UP 10" -> Command.Up(10),
      "down 22" -> Command.Down(22),
      "FoRwArD 1" -> Command.Forward(1),
    )
    for (input, expected) <- examples do
      val actual = Command.parse(input)
      assertEquals(actual, expected)
  }

  test("parse doc example") {
    val expect = List(
      Command.Forward(5),
      Command.Down(5),
      Command.Forward(8),
      Command.Up(3),
      Command.Down(8),
      Command.Forward(2),
    )
    val actual = Command.parseAll(docExample)
    assertEquals(actual, expect)
  }
