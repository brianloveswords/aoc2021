import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

class TestBoard extends ScalaCheckEffectSuite:
  test("cool") {
    assert("cool" == "cool")
  }
