import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite

class TestDay1 extends CatsEffectSuite with ScalaCheckEffectSuite:
  test("no diff when empty list") {
    assertEquals(measureDiff(List.empty), 0)
  }
