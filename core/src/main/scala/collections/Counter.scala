package collections

import cats.kernel.Semigroup
import cats.syntax.semigroup.*
import cats.syntax.foldable.*

case class Counter[A] private (counts: Map[A, Int]):
  require(counts.size > 0, "Counter must have at least one value")

  private case class Min(values: Set[A], count: Int):
    def combine(other: Min): Min =
      if other.count < count then Min(other.values, other.count)
      else if other.count == count then Min(values ++ other.values, count)
      else this

  private object Min:
    def apply(value: A, count: Int): Min = Min(Set(value), count)
    def apply(entry: (A, Int)): Min = Min(Set(entry._1), entry._2)
    given Semigroup[Min] = (a, b) => a.combine(b)

  private case class Max(values: Set[A], count: Int):
    def combine(other: Max): Max =
      if other.count > count then Max(other.values, other.count)
      else if other.count == count then Max(values ++ other.values, count)
      else this

  private object Max:
    def apply(value: A, count: Int): Max = Max(Set(value), count)
    def apply(entry: (A, Int)): Max = Max(Set(entry._1), entry._2)
    given Semigroup[Max] = (a, b) => a.combine(b)

  private lazy val (minData, maxData) = counts
    .map(e => (Min(e), Max(e)))
    .toList
    .combineAllOption
    .getOrElse(throw new RuntimeException("counts expected to be non-empty"))

  def apply(a: A): Int = counts.getOrElse(a, 0)

  lazy val min: A = minData.values.head
  lazy val max: A = maxData.values.head
  lazy val allMin: Set[A] = minData.values
  lazy val allMax: Set[A] = maxData.values
  lazy val isTie: Boolean = max == min
  def uniqueMaxOr(tiebreaker: A): A = if isTie then tiebreaker else max
  def uniqueMinOr(tiebreaker: A): A = if isTie then tiebreaker else min

  def debug =
    println(s"minData: $minData")
    println(s"maxData: $maxData")

object Counter:
  def apply[A](xs: Seq[A]): Counter[A] =
    val counts = xs.foldLeft(Map.empty[A, Int]) { (acc, x) =>
      acc.updatedWith(x) {
        case Some(count) => Some(count + 1)
        case None        => Some(1)
      }
    }
    Counter(counts)
