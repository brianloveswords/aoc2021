import cats.implicits.*
import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop
import org.scalacheck.Prop.*

class TestCollections extends CatsEffectSuite with ScalaCheckEffectSuite:
  test("zipWithPrevious example") {
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

  property("zipWithPrevious property: first element of head is always None") {
    forAll { (xs: List[Int]) =>
      xs.sizeIs > 0 ==> {
        assertEquals(xs.zipWithPrevious.head._1, None)
        true
      }
    }
  }

  property(
    "zipWithPrevious property: list of first tuple element is original list without last item",
  ) {
    forAll(Gen.nonEmptyListOf(arbitrary[Int])) { xs =>
      val expect = xs.reverse.tail.reverse
      val found = xs.zipWithPrevious.map(_._1).collect { case Some(x) => x }
      assertEquals(found, expect)
    }
  }
