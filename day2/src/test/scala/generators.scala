import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen.*
import org.scalacheck.Gen
import Command.*

private val thrustCommand = posNum[Int].map[Command.Thrust](Thrust.apply)
private val aimCommand = posNum[Int].map[Command.Aim](Aim.apply)

given Arbitrary[Command] = Arbitrary(oneOf(thrustCommand, aimCommand))
given Arbitrary[Command.Aim] = Arbitrary(aimCommand)
given Arbitrary[Command.Thrust] = Arbitrary(thrustCommand)
