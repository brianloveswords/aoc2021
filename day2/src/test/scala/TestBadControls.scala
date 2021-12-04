import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

class TestBadControls extends ScalaCheckEffectSuite:
  test("can control sub poorly") {
    val expected = 150
    val result = BadControls.interpret(Command.parseAll(docExample))
    assertEquals(result.value, expected)
  }
