import munit.CatsEffectSuite
import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

val genDescendingInts = Gen
  .nonEmptyListOf(arbitrary[Int])
  .map(_.toSet)
  .filter(_.sizeIs > 1)
  .map(_.toList.sorted.reverse)

val genAscendingInts = Gen
  .nonEmptyListOf(arbitrary[Int])
  .map(_.toSet)
  .filter(_.sizeIs > 1)
  .map(_.toList.sorted)

class TestObserveIncrease extends CatsEffectSuite with ScalaCheckEffectSuite:
  test("no diff when empty list") {
    assertEquals(observeIncrease(List.empty), 0)
  }

  test("no diff when list is single element") {
    assertEquals(observeIncrease(List(1)), 0)
  }

  test("part 1 from docs") {
    assertEquals(observeIncrease(docExample), 7)
  }

  test("part 2 from docs") {
    assertEquals(observeIncrease(docExample, Window(3)), 5)
  }

  property("no diff when list is sorted in descending order") {
    forAll(genDescendingInts) { xs =>
      assertEquals(observeIncrease(xs), 0)
    }
  }

  property("n-1 diff when deduped list is sorted in ascending order") {
    forAllNoShrink(genAscendingInts) { xs =>
      assertEquals(observeIncrease(xs), xs.length - 1)
    }
  }
