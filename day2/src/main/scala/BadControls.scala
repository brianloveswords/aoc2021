case class BadControls(x: Int, y: Int):
  def move(cmd: Command): BadControls = cmd match
    case Command.Thrust(v) => copy(x = x + v)
    case Command.Aim(v)    => copy(y = y + v)

  def value: Int = x * y

object BadControls extends ((Int, Int) => BadControls):
  def empty: BadControls = BadControls(0, 0)
  def interpret(cmds: Seq[Command]): BadControls =
    cmds.foldLeft(BadControls.empty)(_ move _)
