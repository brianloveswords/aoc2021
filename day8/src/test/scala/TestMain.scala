import files.linesFromFile
import munit.ScalaCheckEffectSuite
import org.scalacheck.Prop.*
import org.scalacheck.Arbitrary.*
import org.scalacheck.Gen
import scala.annotation.targetName
import cats.implicits.*

val docExample = """
be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe
edbfga begcd cbg gc gcadebf fbgde acbgfd abcde gfcbed gfec | fcgedb cgb dgebacf gc
fgaebd cg bdaec gdafb agbcfd gdcbef bgcad gfac gcb cdgabef | cg cg fdcagb cbg
fbegcd cbd adcefb dageb afcb bc aefdc ecdab fgdeca fcdbega | efabcd cedba gadfec cb
aecbfdg fbg gf bafeg dbefa fcge gcbea fcaegb dgceab fcbdga | gecf egdcabf bgf bfgea
fgeab ca afcebg bdacfeg cfaedg gcfdb baec bfadeg bafgc acf | gebdcfa ecba ca fadegcb
dbcfg fgd bdegcaf fgec aegbdf ecdfab fbedc dacgb gdcebf gf | cefg dcbef fcge gbcadfe
bdfegc cbegaf gecbf dfcage bdacg ed bedf ced adcbefg gebcd | ed bcgafe cdgba cbgef
egadfb cdbfeg cegd fecab cgb gbdefca cg fgcdab egfdb bfceg | gbdfcae bgc cg cgb
gcafb gcf dcaebfg ecagb gf abcdeg gaef cafbge fdbac fegbdc | fgae cfgab fg bagce
"""
/* 4 digits */

/*
  a - ( 0, 2, 3, 5, 6, 7, 8, 9 )
  b - ( 0, 4, 5, 6, 8, 9 )
  c - ( 0, 1,  )
 */

/* uniqs:
  1, 4, 7, 8
 */

enum Wire:
  case A
  case B
  case C
  case D
  case E
  case F
  case G
import Wire.*

object Wire:
  def all: List[Wire] = List(A, B, C, D, E, F, G)
  def parse(s: Char): Wire = s match
    case 'a' => A
    case 'b' => B
    case 'c' => C
    case 'd' => D
    case 'e' => E
    case 'f' => F
    case 'g' => G

enum Pos:
  case P1
  case P2
  case P3
  case P4
  case P5
  case P6
  case P7
import Pos.*

case class Configuration(
    p1: Wire,
    p2: Wire,
    p3: Wire,
    p4: Wire,
    p5: Wire,
    p6: Wire,
    p7: Wire,
):
  private val found = Set(p1, p2, p3, p4, p5, p6, p7)
  require(
    found.size == 7,
    s"every wire must be used, missing { ${Wire.all.toSet.diff(found).mkString(", ")} }",
  )

  private val segmentMap = Map(
    Set(p1, p2, p3, p5, p6, p7) -> 0,
    Set(p3, p6) -> 1,
    Set(p1, p3, p4, p5, p7) -> 2,
    Set(p1, p3, p4, p6, p7) -> 3,
    Set(p2, p3, p4, p6) -> 4,
    Set(p1, p2, p4, p6, p7) -> 5,
    Set(p1, p2, p4, p5, p6, p7) -> 6,
    Set(p1, p3, p6) -> 7,
    Set(p1, p2, p3, p4, p5, p6, p7) -> 8,
    Set(p1, p2, p3, p4, p6, p7) -> 9,
  )

  def parse(s: String): Option[(Seq[Int], Seq[Int])] =
    val parts = s.trim.split("\\|").toList
    def go(signal: String): Option[Seq[Int]] =
      val empty: Option[Seq[Int]] = Some(Seq.empty)
      signal.trim
        .split("\\s+")
        .toSet
        .map(_.toSet.map(Wire.parse))
        .map { wireSet =>
          val found = segmentMap.get(wireSet)
          println(s"${wireSet} -> ${found}")
          found
        }
        .foldLeft(empty)((acc, d) =>
          println(s"d: ${d}; acc: ${acc}")
          (acc, d) match
            case (None, _)           => None
            case (_, None)           => None
            case (Some(xs), Some(x)) => Some(xs :+ x),
        )
    for
      diagnostic <- go(parts(0))
      output <- go(parts(1))
    yield (diagnostic, output)

  val sizeMap = Map(
    // 2 segments: 1
    2 -> Set(Set(P3, P6)),
    // 3 segments: 7
    3 -> Set(Set(P1, P3, P6)),
    // 4 segments: 4
    4 -> Set(Set(P2, P3, P4, P6)),
    // 5 segments: 2,3,5
    5 -> Set(
      Set(P1, P3, P4, P5, P7),
      Set(P1, P3, P4, P6, P7),
      Set(P1, P2, P4, P6, P7),
    ),
    // 6 segments: 0,6,9
    6 -> Set(
      Set(P1, P2, P3, P5, P6, P7),
      Set(P1, P2, P4, P5, P6, P7),
      Set(P1, P2, P3, P4, P6, P7),
    ),
    // 7 segments: 8
    7 -> Set(Set(P1, P2, P3, P4, P5, P6, P7)),
  )

  def hasConnection(conn: (Pos, Wire)) =
    val (pos, wire) = conn
    pos match
      case P1 => wire == p1
      case P2 => wire == p2
      case P3 => wire == p3
      case P4 => wire == p4
      case P5 => wire == p5
      case P6 => wire == p6
      case P7 => wire == p7

  @targetName("hasConnectionsSet")
  def hasConnection(conn: (Pos, Set[Wire])) =
    val (pos, wires) = conn
    pos match
      case P1 => wires.contains(p1)
      case P2 => wires.contains(p2)
      case P3 => wires.contains(p3)
      case P4 => wires.contains(p4)
      case P5 => wires.contains(p5)
      case P6 => wires.contains(p6)
      case P7 => wires.contains(p7)

  def isPossible(conn: (Set[Pos], Set[Wire])) =
    val (positions, wires) = conn
    positions.forall(pos => hasConnection((pos, wires)))

  def isAnyPossible(conn: (Set[Wire], Set[Set[Pos]])) =
    val (wires, positionSet) = conn
    positionSet.foldLeft(false)((acc, positions) =>
      acc || isPossible((positions, wires)),
    )

  def observed(wires: Set[Wire]): Boolean =
    val positions = sizeMap(wires.size)
    isAnyPossible(wires, positions)

  def observedAll(wireSets: Set[Set[Wire]]): Boolean =
    wireSets.forall(observed)

object Configuration:
  def apply(xs: Seq[Wire]): Configuration =
    require(xs.size == 7, "must have exactly 7 wires")
    Configuration(
      xs(0),
      xs(1),
      xs(2),
      xs(3),
      xs(4),
      xs(5),
      xs(6),
    )

def patternToWires(s: String): Set[Wire] =
  s.map(Wire.parse).toSet

def testToWireSet(s: String): Set[Set[Wire]] =
  s.trim.split("\\|")(0).split("\\s+").map(patternToWires).toSet

class TestMain extends ScalaCheckEffectSuite:
  test("scratch") {
    val diagnostic =
      "abcefg cf acdeg acdfg bcdf abdfg abdefg acf abcdefg abcdfg | cf abdfg abcefg abcdefg"
    // val diagnostic =
    //   "be cfbegad cbdgef fgaecd cgeb fdcge agebfd fecdb fabcd edb | fdgacbe cefdb cefbgd gcbe"
    val wireSets = testToWireSet(diagnostic)
    val configs = Wire.all.permutations.map(Configuration.apply)
    val found = configs.filter(_.observedAll(wireSets)).toSeq

    require(found.size == 1, s"found ${found.size}")
    val c1 = found.head
    println(c1.parse(diagnostic))
    println(c1)

  }

  test("ok") {

    val inputString = linesFromFile("day8/resources/input.txt").mkString("\n")
    val outputs = inputString.trim
      .split("\\n")
      .map(_.trim)
      .filterNot(_.isEmpty)
      .map(_.split("\\s+\\|\\s+").toList)
      .map(_.apply(1))
      .map(_.split("\\s+").toList)
      .toList

  }
