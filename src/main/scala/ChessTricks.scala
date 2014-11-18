import org.rogach.scallop._

class Conf(args: Seq[String]) extends ScallopConf(args) {

  def greaterThanZero(size: Int): Boolean = {
    size > 0
  }

  def greaterEqualZero(size: Int): Boolean = {
    size >= 0
  }

  banner( """Usage: chesstricks <options>
            |
            |Options:
            | """.stripMargin)

  lazy val boardX = opt[Int](short = 'X', validate = greaterThanZero, default = Some(7))
  lazy val boardY = opt[Int](short = 'Y', validate = greaterThanZero, default = Some(7))
  lazy val kings = opt[Int](short = 'K', validate = greaterEqualZero, default = Some(2))
  lazy val queens = opt[Int](short = 'Q', validate = greaterEqualZero, default = Some(2))
  lazy val bishops = opt[Int](short = 'B', validate = greaterEqualZero, default = Some(2))
  lazy val knights = opt[Int](short = 'k', validate = greaterEqualZero, default = Some(1))
  lazy val rooks = opt[Int](short = 'R', validate = greaterEqualZero, default = Some(0))

  lazy val printFirst = opt[Int](validate = greaterEqualZero, default = Some(10))
  lazy val showNumberOfSolutions = toggle(default = Some(true))

}

object ChessTricks {

  def main(args: Array[String]) {

    val conf = new Conf(args)

    val board = Board(conf.boardX(), conf.boardY())
    val solver = Solver(board)

    val pieces = Map(
      King -> conf.kings(),
      Queen -> conf.queens(),
      Bishop -> conf.bishops(),
      Knight -> conf.knights(),
      Rook -> conf.rooks()
    ).filter(_._2 > 0)

    if (pieces.size > 0) {

      println(s"Will solve problem on ${board.X}x${board.Y} board.")
      println(s"Defined pieces are: " + pieces.map(c=> s"${c._1.getClass.getCanonicalName} -> ${c._2}").mkString(", "))

      val solutions = solver.solve(pieces)

      if (conf.printFirst() > 0) {
        println(s"First ${conf.printFirst()} solutions are:")
        solutions.take(conf.printFirst()).foreach {
          solution => board.prettyPrint(solution.combinations)
        }
      }

      if (conf.showNumberOfSolutions()) {

        println(s"Problem has ${solutions.size} solutions total")

      }
    } else {
      conf.printHelp()
    }

  }
}
