package chesstricks

import utest._

object SolverTest extends TestSuite {

  val tests = TestSuite {
    'empty_for_none_pieces {
      val board  = Board(3, 3)
      val solver = Solver(board)
      assert(solver.solve(Map()).size == 0)
    }

    'example1 {

      val board  = Board(3, 3)
      val solver = Solver(board)

      val examplePieces = Map[Piece, Int](
        Piece.King -> 2,
        Piece.Rook -> 1
      )
      val solutions = solver.solve(examplePieces)
      assert(solutions.size == 4)
    }

    'example2 {
      val board  = Board(4, 4)
      val solver = Solver(board)

      val examplePieces = Map[Piece, Int](
        Piece.Rook   -> 2,
        Piece.Knight -> 4
      )

      val solutions = solver.solve(examplePieces)

      assert(solutions.size == 8)
    }

    /*
    'example7x7 {
      val board = Board(7, 7)
      val solver = Solver(board)

      val examplePieces = Map(
        King -> 2,
        Queen -> 2,
        Bishop -> 2,
        Knight -> 1
      )
      val solutions = solver.solve(examplePieces)
      assert(solutions.size == 8)

    }
   */

  }
}
