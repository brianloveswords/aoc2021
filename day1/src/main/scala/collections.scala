import cats.Functor
import cats.syntax.option.*
import cats.syntax.functor.*

extension [T[_]: Functor](xs: T[Int])
  def zipWithPrevious: T[(Option[Int], Int)] =
    var previous = none[Int]
    xs.map { x =>
      val result = (previous, x)
      previous = Some(x)
      result
    }
