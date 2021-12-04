package collections

import cats.implicits.*
import munit.ScalaCheckEffectSuite
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop.*

class TestCounter extends ScalaCheckEffectSuite:
  test("example") {
    val counter = Counter(Seq(1, 1, 1, 0, 2, 2))
    counter.debug
    assertEquals(counter.max, 1)
    assertEquals(counter.min, 0)
    assertEquals(counter(1), 3)
    assertEquals(counter(0), 1)
    assertEquals(counter(2), 2)
  }
