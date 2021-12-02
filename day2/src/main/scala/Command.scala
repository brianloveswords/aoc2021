import cats.kernel.Monoid

enum Command:
  case Forward(v: Int)
  case Up(v: Int)
  case Down(v: Int)

object Command:
  def parse(s: String): Command = s.trim.toLowerCase match
    case s"forward $v" => Forward(v.toInt)
    case s"up $v"      => Up(v.toInt)
    case s"down $v"    => Down(v.toInt)
    case _ => throw new IllegalArgumentException(s"Unknown command: $s")

  def parseAll(s: String): List[Command] =
    s.trim.split("\n").toList.map(Command.parse)
