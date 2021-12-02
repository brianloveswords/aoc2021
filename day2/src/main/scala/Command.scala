enum Command:
  case Thrust(v: Int)
  case Aim(v: Int)

  def isThrust: Boolean = this match
    case Thrust(_) => true
    case _         => false

object Command:
  def parse(s: String): Command = s.trim.toLowerCase match
    case s"forward $v" => Thrust(v.toInt)
    case s"up $v"      => Aim(-v.toInt)
    case s"down $v"    => Aim(v.toInt)
    case _ => throw new IllegalArgumentException(s"Unknown command: $s")

  def parseAll(s: String): List[Command] =
    s.trim.split("\n").toList.map(Command.parse)
