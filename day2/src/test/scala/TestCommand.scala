import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

class TestCommand extends ScalaCheckEffectSuite:
  test("parse examples") {
    val examples = List(
      "UP 10" -> Command.Aim(-10),
      "down 22" -> Command.Aim(22),
      "FoRwArD 1" -> Command.Thrust(1),
    )
    for (input, expected) <- examples do
      val actual = Command.parse(input)
      assertEquals(actual, expected)
  }

  test("parse doc example") {
    val expect = List(
      Command.Thrust(5),
      Command.Aim(5),
      Command.Thrust(8),
      Command.Aim(-3),
      Command.Aim(8),
      Command.Thrust(2),
    )
    val actual = Command.parseAll(docExample)
    assertEquals(actual, expect)
  }
