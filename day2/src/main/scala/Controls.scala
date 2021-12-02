case class Controls(horizontal: Int, depth: Int, aim: Int):
  def move(cmd: Command): Controls = cmd match
    case Command.Aim(v) => copy(aim = aim + v)
    case Command.Thrust(v) =>
      copy(
        horizontal = horizontal + v,
        depth = depth + aim * v,
      )

  def value: Int = horizontal * depth

object Controls extends ((Int, Int, Int) => Controls):
  def empty: Controls = Controls(0, 0, 0)
  def interpret(cmds: Seq[Command]): Controls =
    cmds.foldLeft(Controls.empty)(_ move _)
