import collections.pivot
import scala.annotation.targetName

/*
@main def main(file: String) =
  val rawLines: Seq[String] = file match
    case "example" => docExample.trim.split("\n").toSeq
    case file      => linesFromFile(file)
 */

def parseDraws(line: String): Seq[Int] =
  line.split(",").map(_.toInt)

def parseBoards(lines: Seq[String]): Seq[Board] =
  ???

// @main def main =
//   val rawLines: Seq[String] = file match
//     case "example" => docExample.trim.split("\n").toSeq
//     case file      => linesFromFile(file)

//   println("Hello, world!")
