
object ChessTricks extends App {

  val board = Board(7, 7)
  val solver = Solver(board)

  val examplePieces = Map(
    King -> 2,
    Queen -> 2,
    Bishop -> 2,
    Knight -> 1
  )

  val solutions = solver.solve(examplePieces)

  println(solutions.size)

  //solutions foreach (s=>board.prettyPrint(s.combinations))
}
