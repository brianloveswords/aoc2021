import files.linesFromFile

@main def main(file: String) =
  val rawCommands: Seq[String] = file match
    case "example" => docExample.split("\n").toSeq.filterNot(_.isEmpty)
    case file      => linesFromFile(file).filterNot(_.isEmpty)

  val commands = rawCommands.map(Command.parse)
  val badResult = BadControls.interpret(commands).value
  val goodResult = Controls.interpret(commands).value

  println(s"Bad controls: $badResult")
  println(s"Good controls: $goodResult")

val docExample = """
  forward 5
  down 5
  forward 8
  up 3
  down 8
  forward 2
""".stripMargin
