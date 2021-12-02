import cats.kernel.Monoid

case class GoodControls(horizontal: Int, depth: Int, aim: Int):
  def move(cmd: Command): GoodControls = cmd match
    case Command.Down(v) => copy(aim = aim + v)
    case Command.Up(v)   => copy(aim = aim - v)
    case Command.Forward(v) =>
      copy(
        horizontal = horizontal + v,
        depth = depth + aim * v,
      )

  def value: Int = horizontal * depth

object GoodControls extends ((Int, Int, Int) => GoodControls):
  def empty: GoodControls = GoodControls(0, 0, 0)
  def interpret(cmds: Seq[Command]): GoodControls =
    cmds.foldLeft(GoodControls.empty)(_ move _)
