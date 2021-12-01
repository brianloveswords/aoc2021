import cats.implicits.*

def observeIncrease(xs: List[Int], window: Int = 1): Int = xs
  .sliding(window)
  .toList
  .map(_.sum)
  .zipWithPrevious
  .map {
    case (None, _)    => 0
    case (Some(x), y) => if y > x then 1 else 0
  }
  .toList
  .sum

val docExample = List(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
@main def main(file: String) =
  val xs = file match
    case "example" => docExample
    case file      => io.Source.fromFile(file).getLines.toList.map(_.toInt)

  println(s"1 window ${observeIncrease(xs, 1)}")
  println(s"3 window ${observeIncrease(xs, 3)}")
