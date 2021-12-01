import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.{Arbitrary, Gen}
import cats.implicits.*

class TestCollections extends CatsEffectSuite with ScalaCheckEffectSuite:
  test("zipWithPrevious") {
    assertEquals(
      List(1, 2, 3, 4).zipWithPrevious,
      List(
        (None, 1),
        (Some(1), 2),
        (Some(2), 3),
        (Some(3), 4),
      ),
    )
  }
