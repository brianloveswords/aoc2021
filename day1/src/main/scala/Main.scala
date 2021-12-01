import cats.implicits.*

val docExample = List(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)

opaque type Window = Int
object Window extends (Int => Window):
  def apply(n: Int): Window = n

def observeIncrease(xs: List[Int], window: Window = 1): Int = xs
  .sliding(window)
  .map(_.sum)
  .zipWithPrevious
  .map {
    case (None, _)    => 0
    case (Some(x), y) => if y > x then 1 else 0
  }
  .sum

@main def main(file: String) =
  val xs = file match
    case "example" => docExample
    case file      => io.Source.fromFile(file).getLines.toList.map(_.toInt)

  println(s"1 window ${observeIncrease(xs, 1)}")
  println(s"3 window ${observeIncrease(xs, 3)}")
