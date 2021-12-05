import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

class TestMain extends ScalaCheckEffectSuite:
  property("parse valid points") {
    forAll { (x: Int, y: Int) =>
      val input = s"$x,$y"
      val point = Point.parse(input)
      assertEquals(point, Point(x, y))
    }
  }

  property("parse valid lines") {
    forAll { (x1: Int, y1: Int, x2: Int, y2: Int) =>
      val input = s"$x1,$y1 -> $x2,$y2"
      val line = Line.parse(input)
      assertEquals(line, Line(Point(x1, y1), Point(x2, y2)))
    }
  }

  test("show all points") {
    val points = Line(Point(1, 0), Point(9, 0)).coverage
    val expected = Seq(
      Point(1, 0),
      Point(2, 0),
      Point(3, 0),
      Point(4, 0),
      Point(5, 0),
      Point(6, 0),
      Point(7, 0),
      Point(8, 0),
      Point(9, 0),
    )
    assertEquals(points, expected)
  }

  test("show all points: ex 1") {
    val points = Line(Point(1, 1), Point(1, 3)).coverage
    val expected = Seq(
      Point(1, 1),
      Point(1, 2),
      Point(1, 3),
    )
    assertEquals(points, expected)
  }

  test("show all points: ex 2") {
    val points = Line(Point(9, 7), Point(7, 7)).coverage
    val expected = Seq(
      Point(7, 7),
      Point(8, 7),
      Point(9, 7),
    )
    assertEquals(points, expected)
  }

  test("parse example") {
    val expect = Seq(
      Line(Point(0, 9), Point(5, 9)),
      Line(Point(8, 0), Point(0, 8)),
      Line(Point(9, 4), Point(3, 4)),
      Line(Point(2, 2), Point(2, 1)),
      Line(Point(7, 0), Point(7, 4)),
      Line(Point(6, 4), Point(2, 0)),
      Line(Point(0, 9), Point(2, 9)),
      Line(Point(3, 4), Point(1, 4)),
      Line(Point(0, 0), Point(8, 8)),
      Line(Point(5, 5), Point(8, 2)),
    )

    val result = docExample
      .split("\n")
      .filterNot(_.isEmpty)
      .map(Line.parse)
      .toSeq

    assertEquals(result, expect)
  }

  test("show map") {
    val lines = docExample
      .split("\n")
      .filterNot(_.isEmpty)
      .map(Line.parse)
      .toSeq

    val ventMap = VentMap(lines)
    println(ventMap.toString)
  }
