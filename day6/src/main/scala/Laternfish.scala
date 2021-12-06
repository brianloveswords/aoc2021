import files.linesFromFile

case class Laternfish(timer: Int):
  override def toString: String =
    s"$timer"

  lazy val tick: Seq[Laternfish] = timer match
    case 0 => Seq(Laternfish(6), Laternfish(8))
    case n => Seq(Laternfish(n - 1))

  def tickN(n: Int): Seq[Laternfish] =
    require(n >= 0)
    def go(n: Int, acc: Seq[Laternfish]): Seq[Laternfish] =
      if n == 0 then acc
      else go(n - 1, acc.flatMap(_.tick))
    go(n, Seq(this))

object Laternfish:
  def parse(s: String): Laternfish =
    Laternfish(s.toInt)