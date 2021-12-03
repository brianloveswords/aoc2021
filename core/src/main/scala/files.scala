package files

def linesFromFile(filename: String): Seq[String] =
  io.Source.fromFile(filename).getLines.toSeq
