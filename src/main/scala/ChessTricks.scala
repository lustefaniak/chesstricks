import org.rogach.scallop._

class Conf(args: Seq[String]) extends ScallopConf(args) {

  def greaterThanZero(size: Int): Boolean = {
    if (size > 0) true else false
  }

  def greaterEqualZero(size: Int): Boolean = {
    if (size >= 0) true else false
  }

  banner( """Usage: chesstricks <options>
            |
            |Options:
            | """.stripMargin)


  val boardX = opt[Int](short = 'X', validate = greaterThanZero, default = Some(7))
  val boardY = opt[Int](short = 'Y', validate = greaterThanZero, default = Some(7))
  val kings = opt[Int](short = 'K', validate = greaterEqualZero, default = Some(2))
  val queens = opt[Int](short = 'Q', validate = greaterEqualZero, default = Some(2))
  val bishops = opt[Int](short = 'B', validate = greaterEqualZero, default = Some(2))
  val knights = opt[Int](short = 'k', validate = greaterEqualZero, default = Some(1))
  val rocks = opt[Int](short = 'T', validate = greaterEqualZero, default = Some(0))

  val printFirst = opt[Int](validate = greaterEqualZero, default = Some(10))
  val showNumberOfSolutions = toggle(default = Some(true))

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
      Rock -> conf.rocks()
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
