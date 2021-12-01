import cats.kernel.Monoid

def observeIncrease(input: List[Int], window: Int = 1): Int =
  val windowed = input
    .sliding(window)
    .map(_.sum)
    .toList

  windowed.zipWithIndex.map { (x, i) =>
    if i == 0 then 0
    else if
      val left = windowed(i - 1)
      val right = x
      right > left
    then 1
    else 0
  }.sum

val docExample = List(199, 200, 208, 210, 200, 207, 240, 269, 260, 263)
@main def main(file: String) =
  // val xs = io.Source.fromFile(file).getLines.toList.map(_.toInt)
  val xs = docExample
  println(observeIncrease(xs, 3))
