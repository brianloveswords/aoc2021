import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

class TestMain extends ScalaCheckEffectSuite:
  test("ok") {
    val school = School.parse("3")
    println(school.tick(3))
  }
