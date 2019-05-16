import org.rogach.scallop._
import scala.collection.immutable._

class Conf(args: Array[String]) extends ScallopConf(args) {

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

  val boardX = opt[Int]("boardX", short = 'X', validate = greaterThanZero, default = Some(7))
  val boardY = opt[Int]("boardY", short = 'Y', validate = greaterThanZero, default = Some(7))
  val kings = opt[Int]("kings",short = 'K', validate = greaterEqualZero, default = Some(2))
  val queens = opt[Int]("queens", short = 'Q', validate = greaterEqualZero, default = Some(2))
  val bishops = opt[Int]("bishops", short = 'B', validate = greaterEqualZero, default = Some(2))
  val knights = opt[Int]("knights", short = 'k', validate = greaterEqualZero, default = Some(1))
  val rooks = opt[Int]("rooks", short = 'R', validate = greaterEqualZero, default = Some(0))

  val printFirst = opt[Int]("printFirst", validate = greaterEqualZero, default = Some(10))
  val showNumberOfSolutions = toggle("showNumberOfSolutions", default = Some(true))

  verify()
}

object ChessTricks {

  def main(args: Array[String]) {

    val conf = new Conf(args)

    val board = Board(conf.boardX(), conf.boardY())
    val solver = Solver(board)

    val pieces: Map[Piece, Int] = Map[Piece, Int](
      Piece.King -> conf.kings(),
      Piece.Queen -> conf.queens(),
      Piece.Bishop -> conf.bishops(),
      Piece.Knight -> conf.knights(),
      Piece.Rook -> conf.rooks()
    ).filter(_._2 > 0)

    if (pieces.size > 0) {

      println(s"Will solve problem on ${board.X}x${board.Y} board.")
      println(s"Defined pieces are: " + pieces.map(c=> s"${c._1} -> ${c._2}").mkString(", "))

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
