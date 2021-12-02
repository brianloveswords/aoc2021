import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen.*

val genCommand = for
  n <- posNum[Int]
  gen <- oneOf(
    Command.Aim(n),
    Command.Thrust(n),
  )
yield gen

val genForwardCommand =
  for n <- posNum[Int]
  yield Command.Thrust(n)

given Arbitrary[Command] = Arbitrary(genCommand)
