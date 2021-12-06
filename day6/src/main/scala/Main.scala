import files.linesFromFile
import scala.collection.immutable.LazyList

val docExample = "3,4,3,1,2"

@main def main(file: String) =
  val input: String = file match
    case "example" => docExample
    case file      => linesFromFile(file).mkString("\n")

  println(School.parse(input).tick(256).size)
