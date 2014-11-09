
object ChessTricks extends App {

  val board = Board(7, 7)
  val solver = Solver(board)

  val examplePieces = Map(
    King -> 2,
    Queen -> 2,
    Bishop -> 2,
    Knight -> 1
  )

  val firstToPrint = 100

  val solutions = solver.solve(examplePieces)

  println(s"Example 7x7 has ${solutions.size} solutions")
  println(s"First $firstToPrint are:")
  solutions.take(firstToPrint).foreach {
    solution => board.prettyPrint(solution.combinations)
  }
}
