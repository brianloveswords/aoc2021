val docExample = """
00100
11110
10110
10111
10101
01111
00111
11100
10000
11001
00010
01010
"""

def rotate(xs: List[List[Int]]): List[List[Int]] =
  val width = xs(0).length
  (for (i <- 0 until width) yield xs.map(_(i))).toList

def mostCommon(xs: List[Int]): Int = counts(xs).maxBy(_._2)._1

def leastCommon(xs: List[Int]): Int = counts(xs).minBy(_._2)._1

def counts(xs: List[Int]): Map[Int, Int] =
  xs.groupBy(identity).mapValues(_.size).toMap

def getOxygenReading(diagnostic: List[List[Int]]): Int =
  def go(xs: List[List[Int]], bit: Int = 0): Int =
    if xs.sizeIs == 1 then Integer.parseInt(xs.head.mkString, 2)
    else
      val count = rotate(xs).map(counts)(bit)
      val filterBit = if count(0) > count(1) then 0 else 1
      val remaining = xs.filter(_(bit) == filterBit)
      println(s"bit: $bit\nfilterBit: $filterBit\nremaining: $remaining\n\n")
      go(remaining, bit + 1)
  go(diagnostic)

def getCo2Reading(diagnostic: List[List[Int]]): Int =
  def go(xs: List[List[Int]], bit: Int = 0): Int =
    if xs.sizeIs == 1 then Integer.parseInt(xs.head.mkString, 2)
    else
      val count = rotate(xs).map(counts)(bit)
      val filterBit = if count(1) < count(0) then 1 else 0
      val remaining = xs.filter(_(bit) == filterBit)
      println(s"bit: $bit\nfilterBit: $filterBit\nremaining: $remaining\n\n")
      go(remaining, bit + 1)
  go(diagnostic)

@main def main(file: String) =
  val rawLines: List[String] = file match
    case "example" => docExample.trim.split("\n").toList
    case path      => io.Source.fromFile(path).getLines.toList
  val lines = rawLines.map(_.trim.toList.map(_.toString.toInt))
  println(part2(lines))
// println(part1(lines))

def part2(lines: List[List[Int]]): Unit =
  val oxygen = getOxygenReading(lines)
  val co2 = getCo2Reading(lines)
  println(s"Oxygen: $oxygen")
  println(s"CO2: $co2")
  println(oxygen * co2)

def part1(lines: List[List[Int]]): Unit =
  val rotated = rotate(lines)
  println(rotated)
  val gamma = Integer.parseInt(rotated.map(mostCommon).mkString, 2)
  println(gamma)
  val epsilon = Integer.parseInt(rotated.map(leastCommon).mkString, 2)
  println(epsilon)
  println(gamma * epsilon)
