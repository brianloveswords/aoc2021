import collections.pivot
import files.linesFromFile
import scala.annotation.targetName
import cats.syntax.option.*

@main def main(file: String) =
  val input: String = file match
    case "example" => docExample
    case file      => linesFromFile(file).mkString("\n")

  strategy1(input)
  strategy2(input)

// win first
def strategy1(input: String) =
  val (draws, boards) = parseInput(input)
  val winData = draws.foldLeft((Seq.empty[Board], boards)) { (acc, draw) =>
    acc match
      case (Seq(), boards) =>
        val updatedBoards = boards.map(_.updated(draw))
        val winningSet = updatedBoards.filter(_.winningDraw.isDefined).toSeq
        (winningSet, updatedBoards)
      case otherwise => otherwise
  }

  println(winData._1.map(_.score))

// win last
def strategy2(input: String) =
  val (draws, boards) = parseInput(input)
  val winData = draws.foldLeft((none[Board], boards)) { (acc, draw) =>
    val (currentWinner, currentBoards) = acc
    val nextBoards = currentBoards.map(_.updated(draw))
    val nextWinner = nextBoards.find(_.isWon)
    if nextWinner.isEmpty then (currentWinner, nextBoards)
    else (nextWinner, nextBoards.filterNot(_.isWon))
  }

  println(winData._1.get.score)

val docExample = """
7,4,9,5,11,17,23,2,0,14,21,24,10,16,13,6,15,25,12,22,18,20,8,19,3,26,1

22 13 17 11  0
 8  2 23  4 24
21  9 14 16  7
 6 10  3 18  5
 1 12 20 15 19

 3 15  0  2 22
 9 18 13 17  5
19  8  7 25 23
20 11 10 24  4
14 21 16 12  6

14 21 17 24  4
10 16 15  9 19
18  8 23 26 20
22 11 13  6  5
 2  0 12  3  7
"""
