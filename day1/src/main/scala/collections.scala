// Collection utilities
import cats.implicits.*
import scala.annotation.targetName
import cats.Functor

extension [T[_]: Functor](xs: T[Int])
  def zipWithPrevious: T[(Option[Int], Int)] =
    var previous = none[Int]
    xs.map { x =>
      val result = (previous, x)
      previous = Some(x)
      result
    }
