import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen.*

class TestControls extends ScalaCheckEffectSuite:
  test("can control sub well") {
    val expected = 900
    val result = Controls.interpret(Command.parseAll(docExample))
    assertEquals(result.value, expected)
  }

  property("going only forward won't change depth") {
    forAll(nonEmptyListOf(arbitrary[Command.Thrust])) { commands =>
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

  property(
    "continuous sequences of aims are commutative",
  ) {
    forAll(
      nonEmptyListOf(arbitrary[Command.Aim]),
      arbitrary[Command.Thrust],
    ) { (aims, thrust) =>
      import scala.util.Random

      val shuffledAims = Random.shuffle(aims)

      val list1 = (thrust :: aims).reverse
      val list2 = (thrust :: shuffledAims).reverse

      val result1 = Controls.interpret(list1)
      val result2 = Controls.interpret(list2)

      assertEquals(result1, result2)
    }
  }

  property(
    "continuous sequences of aims can be collapsed",
  ) {
    forAll(
      nonEmptyListOf(arbitrary[Command.Aim]),
      arbitrary[Command.Thrust],
      arbitrary[Command.Thrust],
    ) { (aims, thrust1, thrust2) =>
      val combinedAim = Controls.interpret(aims).toAim

      val list1 = List(thrust1) ++ aims ++ List(thrust2)
      val list2 = List(thrust1, combinedAim, thrust2)

      val result1 = Controls.interpret(list1)
      val result2 = Controls.interpret(list2)

      assertEquals(result1, result2)
    }
  }

  property(
    "continuous sequences of thrusts are commutative",
  ) {
    forAll(
      nonEmptyListOf(arbitrary[Command.Thrust]),
      arbitrary[Command.Aim],
    ) { (thrusts, aim) =>
      import scala.util.Random

      val shuffledThrusts = Random.shuffle(thrusts)

      val list1 = aim :: thrusts
      val list2 = aim :: shuffledThrusts

      val result1 = Controls.interpret(list1)
      val result2 = Controls.interpret(list2)

      assertEquals(result1, result2)
    }
  }

  property(
    "continuous sequences of thrusts can be collapsed",
  ) {
    forAll(
      nonEmptyListOf(arbitrary[Command.Thrust]),
      arbitrary[Command.Thrust],
    ) { (thrusts, aim) =>
      val combinedThrust = Controls.interpret(thrusts).toThrust

      val list1 = aim :: thrusts
      val list2 = List(aim, combinedThrust)

      val result1 = Controls.interpret(list1)
      val result2 = Controls.interpret(list2)

      assertEquals(result1, result2)
    }
  }
