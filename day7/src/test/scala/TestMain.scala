import files.linesFromFile
import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen

val docExample = "16,1,2,0,4,2,7,1,2,14"
val input = linesFromFile("day7/resources/input.txt").mkString("\n")

case class Crabmarine(pos: Int, fuelUse: Int = 0):
  def moveToNaive(newPos: Int): Crabmarine =
    copy(pos = newPos, fuelUse = fuelUse + math.abs(newPos - pos))

  def moveToAccurate(newPos: Int): Crabmarine =
    val newFuelUse = (0 to math.abs(newPos - pos)).sum
    copy(pos = newPos, fuelUse = fuelUse + newFuelUse)

object Crabmarine:
  def parse(s: String): Crabmarine =
    Crabmarine(s.trim.toInt)

  def parseList(s: String): Seq[Crabmarine] =
    s.trim.split(",").toSeq.map(parse)

class TestMain extends ScalaCheckEffectSuite:
  test("part1") {
    val crabmarines = Crabmarine.parseList(input)

    val minPos = crabmarines.map(_.pos).min
    val maxPos = crabmarines.map(_.pos).max
    val posRange = minPos to maxPos

    val outcomes = posRange.map { pos =>
      (pos, crabmarines.map(_.moveToNaive(pos).fuelUse).sum)
    }
    println(outcomes.minBy(_._2))
  }

  test("part2") {
    val crabmarines = Crabmarine.parseList(input)

    val minPos = crabmarines.map(_.pos).min
    val maxPos = crabmarines.map(_.pos).max
    val posRange = minPos to maxPos

    val outcomes = posRange.map { pos =>
      (pos, crabmarines.map(_.moveToAccurate(pos).fuelUse).sum)
    }
    println(outcomes.minBy(_._2))

  }
