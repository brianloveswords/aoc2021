package collections

import cats.Functor
import cats.syntax.option.*
import cats.syntax.functor.*

given Functor[Iterator] = new Functor[Iterator] {
  def map[A, B](fa: Iterator[A])(f: A => B): Iterator[B] = fa.map(f)
}

extension [A, T[_]: Functor](xs: T[A])
  /**
   * returns (a, b) pairs where a is an Option of the previous element and b
   * is the current element
   */
  def zipWithPrevious: T[(Option[A], A)] =
    var previous = none[A]
    xs.map { x =>
      val result = (previous, x)
      previous = Some(x)
      result
    }

/**
 * pivot a 2-dimensional sequence so the x and y get swapped.
 *
 * for example, turns the following:
 *
 * ```scala
 * List(
 *  List(1, 2, 3),
 *  List(4, 5, 6),
 * )
 * ```
 *
 * into the following:
 *
 * ```scala
 * List(
 *   List(1, 4),
 *   List(2, 5),
 *   List(3, 6),
 * )
 * ```
 */
def pivot[A](xs: Seq[Seq[A]]): Seq[Seq[A]] =
  val width = xs(0).length
  (for (i <- 0 until width) yield xs.map(_(i))).toSeq
