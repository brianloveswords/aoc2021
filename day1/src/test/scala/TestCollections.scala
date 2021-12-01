import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.{Arbitrary, Gen}
import org.scalacheck.Arbitrary.arbitrary
import cats.implicits.*
import org.scalacheck.Prop

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

  property("zipWithPrevious property: head of left side is always None") {
    forAll { (xs: List[Int]) =>
      xs.sizeIs > 0 ==> {
        assertEquals(xs.zipWithPrevious.head._1, None)
        true
      }
    }
  }

  property(
    "zipWithPrevious property: left side of tuple is original list without last element",
  ) {
    forAll(Gen.nonEmptyListOf(arbitrary[Int])) { xs =>
      val expect = xs.reverse.tail.reverse
      val found = xs.zipWithPrevious.map(_._1).collect { case Some(x) => x }
      assertEquals(found, expect)
    }
  }
