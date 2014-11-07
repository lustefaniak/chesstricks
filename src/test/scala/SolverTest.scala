import org.scalatest.{FlatSpec, Matchers}

import scala.collection.immutable.BitSet


class SolverTest extends FlatSpec with Matchers {

  "solve" should "work for example case#1" in {

    val board = Board(3,3)
    val solver = Solver(board)

    val examplePieces = Map(
      King -> 2,
      Rock -> 1
    )

    val solutions = solver.solve(examplePieces)

    solutions should have size (4)

    //solutions foreach (s=>board.prettyPrint(s.combinations))
  }

  it should "also work for second example" in {
    val board = Board(4, 4)
    val solver = Solver(board)

    val examplePieces = Map(
      Rock -> 2,
      Knight -> 4
    )

    val solutions = solver.solve(examplePieces)

    solutions should have size (8)

    //solutions foreach (s=>board.prettyPrint(s.combinations))
  }

//  it should "not explode for 7x7 board from example" in {
//    val board = Board(7, 7)
//    val solver = Solver(board)
//
//    val examplePieces = Map(
//      King -> 2,
//      Queen -> 2,
//      Bishop -> 2,
//      Knight -> 1
//    )
//
//    val solutions = solver.solve(examplePieces)
//
//    solutions should have size (8)
//  }

}
