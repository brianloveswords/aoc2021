import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

class TestGoodControls extends ScalaCheckEffectSuite:
  test("can control sub well") {
    val expected = 900
    val result = GoodControls.interpret(Command.parseAll(docExample))
    assertEquals(result.value, expected)
  }
