package collections

import cats.Functor
import cats.syntax.functor.*
import cats.syntax.option.*

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
def pivot[A](table: Seq[Seq[A]]): Seq[Seq[A]] =
  if table.isEmpty then table
  else
    val width = table(0).size
    val result =
      for i <- 0 until width
      yield for row <- table if row.size >= i
      yield row(i)
    result.toSeq
