import cats.Functor
import cats.syntax.option.*
import cats.syntax.functor.*

given Functor[Iterator] = new Functor[Iterator] {
  def map[A, B](fa: Iterator[A])(f: A => B): Iterator[B] = fa.map(f)
}

extension [A, T[_]: Functor](xs: T[A])
  def zipWithPrevious: T[(Option[A], A)] =
    var previous = none[A]
    xs.map { x =>
      val result = (previous, x)
      previous = Some(x)
      result
    }
