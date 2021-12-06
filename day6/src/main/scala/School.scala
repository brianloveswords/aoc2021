import files.linesFromFile

case class School(
    t0: Long,
    t1: Long,
    t2: Long,
    t3: Long,
    t4: Long,
    t5: Long,
    t6: Long,
    t7: Long,
    t8: Long,
):
  lazy val tick: School = School(t1, t2, t3, t4, t5, t6, (t7 + t0), t8, t0)
  lazy val size: Long = t0 + t1 + t2 + t3 + t4 + t5 + t6 + t7 + t8
  def tick(n: Int): School = (0 until n).foldLeft(this)((acc, _) => acc.tick)
  def combine(other: School) =
    School(
      t0 + other.t0,
      t1 + other.t1,
      t2 + other.t2,
      t3 + other.t3,
      t4 + other.t4,
      t5 + other.t5,
      t6 + other.t6,
      t7 + other.t7,
      t8 + other.t8,
    )

object School:
  def empty: School = School(0, 0, 0, 0, 0, 0, 0, 0, 0)
  def fromAge(n: Long) = n match
    case 0 => School(1, 0, 0, 0, 0, 0, 0, 0, 0)
    case 1 => School(0, 1, 0, 0, 0, 0, 0, 0, 0)
    case 2 => School(0, 0, 1, 0, 0, 0, 0, 0, 0)
    case 3 => School(0, 0, 0, 1, 0, 0, 0, 0, 0)
    case 4 => School(0, 0, 0, 0, 1, 0, 0, 0, 0)
    case 5 => School(0, 0, 0, 0, 0, 1, 0, 0, 0)
    case 6 => School(0, 0, 0, 0, 0, 0, 1, 0, 0)
    case 7 => School(0, 0, 0, 0, 0, 0, 0, 1, 0)
    case 8 => School(0, 0, 0, 0, 0, 0, 0, 0, 1)
    case _ => throw new Exception("Age must be between 0 and 8")

  def parse(s: String): School =
    s.split(",").map(n => fromAge(n.trim.toLong)).reduce(_ combine _)
