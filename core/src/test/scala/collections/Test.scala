package collections

import cats.implicits.*
import munit.ScalaCheckEffectSuite
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalacheck.Prop.*

class Test_zipWithPrevious extends ScalaCheckEffectSuite:
  test("example") {
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

  property("property: first element of head is always None") {
    forAll { (xs: List[Int]) =>
      xs.sizeIs > 0 ==> {
        assertEquals(xs.zipWithPrevious.head._1, None)
        true
      }
    }
  }

  property(
    "property: list of first tuple element is original list without last item",
  ) {
    forAll(Gen.nonEmptyListOf(arbitrary[Int])) { xs =>
      val expect = xs.reverse.tail.reverse
      val found =
        xs.zipWithPrevious.map(_._1).collect { case Some(x) => x }
      assertEquals(found, expect)
    }
  }

class Test_pivot extends ScalaCheckEffectSuite:
  def genMatrix[A: Arbitrary]: Gen[List[List[A]]] = for {
    n <- Gen.posNum[Int]
    matrix <- Gen.listOfN(n, Gen.listOfN(n, arbitrary[A]))
  } yield matrix

  test("example") {
    assertEquals(
      pivot(
        Seq(
          Seq(1, 2, 3),
          Seq(4, 5, 6),
        ),
      ),
      Seq(
        Seq(1, 4),
        Seq(2, 5),
        Seq(3, 6),
      ),
    )
  }

  property("pivot is reversable") {
    forAll(genMatrix[Int]) { (xs: Seq[Seq[Int]]) =>
      assertEquals(pivot(pivot(xs)), xs)
    }
  }
