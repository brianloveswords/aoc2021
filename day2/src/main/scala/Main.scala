import cats.kernel.Monoid

enum Command:
  case Forward(v: Int)
  case Up(v: Int)
  case Down(v: Int)

case class PositionWrong(x: Int, y: Int):
  def move(cmd: Command): PositionWrong = cmd match
    case Command.Forward(v) => copy(x = x + v)
    case Command.Up(v)      => copy(y = y - v)
    case Command.Down(v)    => copy(y = y + v)

  def value: Int = x * y

object PositionWrong extends ((Int, Int) => PositionWrong):
  def empty: PositionWrong = PositionWrong(0, 0)

  given Monoid[PositionWrong] = new Monoid[PositionWrong] {
    def empty: PositionWrong = PositionWrong.empty
    def combine(x: PositionWrong, y: PositionWrong): PositionWrong =
      PositionWrong(x.x + y.x, x.y + y.y)
  }

object Command:
  def parse(s: String): Command = s.trim.toLowerCase match
    case s"forward $v" => Forward(v.toInt)
    case s"up $v"      => Up(v.toInt)
    case s"down $v"    => Down(v.toInt)
    case _ => throw new IllegalArgumentException(s"Unknown command: $s")

@main def main() = println("cool")

def controlSubWrong(cmds: List[Command]): Int =
  cmds.foldLeft(PositionWrong.empty)(_ move _).value

def parseCommandList(s: String): List[Command] =
  s.trim.split("\n").toList.map(Command.parse)

val docExample = """
  forward 5
  down 5
  forward 8
  up 3
  down 8
  forward 2
""".stripMargin
