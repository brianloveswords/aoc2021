import files.linesFromFile
import scala.annotation.targetName

@main def main(file: String) =
  val input: Seq[String] = file match
    case "example" => docExample.split("\n")
    case file      => linesFromFile(file)

  val lines = input
    .filterNot(_.isEmpty)
    .map(Line.parse)
    .toSeq

  val ventMap = VentMap(lines)
  println(ventMap.dangerPoints.size)

def absRange(a: Int, b: Int): Seq[Int] =
  if a > b then b to a
  else a to b

val docExample = """

0,9 -> 5,9
8,0 -> 0,8
9,4 -> 3,4
2,2 -> 2,1
7,0 -> 7,4
6,4 -> 2,0
0,9 -> 2,9
3,4 -> 1,4
0,0 -> 8,8
5,5 -> 8,2

"""
