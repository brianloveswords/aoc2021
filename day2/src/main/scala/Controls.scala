case class Controls(horizontal: Int, depth: Int, aim: Int, thrust: Int):
  override def toString =
    s"Controls(horizontal=$horizontal, depth=$depth, aim=$aim, thrust=$thrust)"

  def toAim: Command.Aim = Command.Aim(aim)

  def toThrust: Command.Thrust = Command.Thrust(thrust)

  def move(cmd: Command): Controls = cmd match
    case Command.Aim(v) => copy(aim = aim + v)
    case Command.Thrust(v) =>
      copy(
        thrust = thrust + v,
        horizontal = horizontal + v,
        depth = depth + aim * v,
      )

  def value: Int = horizontal * depth

object Controls extends ((Int, Int, Int, Int) => Controls):
  def empty: Controls = Controls(0, 0, 0, 0)
  def interpret(cmds: Seq[Command]): Controls =
    cmds.foldLeft(Controls.empty)(_ move _)
