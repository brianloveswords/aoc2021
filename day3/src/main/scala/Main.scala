import collections.Counter
import collections.pivot
import files.linesFromFile

import scala.annotation.tailrec

type Diagnostic = Seq[Seq[Int]]
type FilterBitFn = Counter[Int] => Int

def mode(xs: Seq[Int]): Int = Counter(xs).max
def antiMode(xs: Seq[Int]): Int = Counter(xs).min
def parseBinary(s: String): Int = Integer.parseInt(s, 2)
def oxygenRating(diagnostic: Diagnostic): Int = rating(diagnostic, _.max(1))
def co2Rating(diagnostic: Diagnostic): Int = rating(diagnostic, _.min(0))

def rating(diagnostic: Diagnostic, fn: FilterBitFn): Int =
  @tailrec
  def go(remaining: Diagnostic, bitPos: Int = 0): Int =
    if remaining.sizeIs == 1 then parseBinary(remaining.head.mkString)
    else
      val counter = pivot(remaining).map(Counter.apply)(bitPos)
      val filterBit = fn(counter)
      val next = remaining.filter(_(bitPos) == filterBit)
      // println(s"bit: $bit\nfilterBit: $filterBit\nremaining: $remaining\n\n")
      go(next, bitPos + 1)
  go(diagnostic)

@main def main(file: String) =
  val rawLines: Seq[String] = file match
    case "example" => docExample.trim.split("\n").toSeq
    case file      => linesFromFile(file)

  val diagnostic = for
    rawLine <- rawLines
    line = rawLine.trim
    if line.nonEmpty
    bits = for char <- line yield char.toString.toInt
  yield bits

  part1(diagnostic)
  part2(diagnostic)

def part1(diagnostic: Diagnostic): Unit =
  // we need to find most common bit in each *column* so we rotate the
  // diagnostic, then we can use regular collection methods.
  val pivoted = pivot(diagnostic)
  val modeBits = pivoted.map(mode)
  val antiModeBits = pivoted.map(antiMode)
  val gamma = parseBinary(modeBits.mkString)
  val epsilon = parseBinary(antiModeBits.mkString)

  println(s"gamma: $gamma")
  println(s"epsilon: $epsilon")
  println(gamma * epsilon)

def part2(diagnostic: Diagnostic): Unit =
  val oxygen = oxygenRating(diagnostic)
  val co2 = co2Rating(diagnostic)

  println(s"Oxygen: $oxygen")
  println(s"CO2: $co2")
  println(oxygen * co2)

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
