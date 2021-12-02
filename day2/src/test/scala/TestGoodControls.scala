import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen.*

class TestGoodControls extends ScalaCheckEffectSuite:
  test("can control sub well") {
    val expected = 900
    val result = Controls.interpret(Command.parseAll(docExample))
    assertEquals(result.value, expected)
  }

  property("going only forward won't change depth") {
    forAll(nonEmptyListOf(genForwardCommand)) { commands =>
      val result = Controls.interpret(commands)
      assertEquals(result.depth, 0)
      assert(result.horizontal > 0, "horizontal must be positive")
    }
  }

  property("never going forward will not change horizontal or depth") {
    forAll { (commands: List[Command]) =>
      val filteredCommands = commands.filterNot(_.isThrust)
      val result = Controls.interpret(filteredCommands)
      assertEquals(result.horizontal, 0)
      assertEquals(result.depth, 0)
    }
  }
