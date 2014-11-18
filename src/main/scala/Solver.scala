import scala.collection.immutable.BitSet

object Solver {

  case class Solution(combinations: Map[Piece, BitSet])

}

trait SolverPieceOrderer {

  /**
   * Default method for sorting piece in order which would reduce depth of search.
   *
   * By default uses {Piece.estimateAverageCapturedFields} to guess which group of pieces
   * would yield best results when used before others.
   *
   * @param pieces
   * @param board
   * @return
   */
  def sortPieces(pieces: Map[Piece, Int], board:Board) = pieces.toList.sortBy {
    case (piece, numberOf) =>
      val estimate = piece.estimateAverageCapturedFields(numberOf)(board)
      //println(s"$piece $numberOf $estimate")
      -estimate
  }
}

case class Solver(val board: Board) extends SolverPieceOrderer{

  import Solver._

  /**
   * Recursivelly takes tries to place $numberOf pieces of $piece type on board
   * Using returned combination with found position and set of still available places
   * tries to put next type of piece in free spots.
   *
   * When all pieces are placed new element of the Stream is emited.
   *
   * @param piecesLeftToPut
   * @param availablePlaces
   * @param alreadyPlacedPieces
   * @param solutionAccumulator
   * @return
   */
  protected def recursiveSolver(piecesLeftToPut: List[(Piece, Int)], availablePlaces: BitSet = board.bitsetWithAllFields,
                                alreadyPlacedPieces: BitSet = BitSet(), solutionAccumulator: Map[Piece, BitSet] = Map()
                                 ): Stream[Solution] = piecesLeftToPut match {

    case (piece, numberOf) :: rest =>
      val leftToPut = rest.map(_._2).sum
      PieceCombinator(piece, numberOf, board).combineReturningAvailablePlaces(availablePlaces, alreadyPlacedPieces).flatMap {
        case PieceCombinator.Combination(positionsPlaces, availablePlacesAfter) if (availablePlacesAfter.size >= leftToPut) =>
          val placesWithPiece = alreadyPlacedPieces | positionsPlaces

          recursiveSolver(rest, availablePlacesAfter, placesWithPiece, solutionAccumulator + (piece -> positionsPlaces))

        case _ => Stream.empty
      }

    case Nil =>
      if (solutionAccumulator.isEmpty) Stream.empty else Stream(Solution(solutionAccumulator))

  }


  /**
   * Solves the problem of placing pieces on a board, by sorting them in order, which
   * hopefully would reduce depth of search and then uses {recursiveSolver} to find
   * all the solutions which are possible on the board
   *
   * @param pieces
   * @return
   */
  def solve(pieces: Map[Piece, Int]): Stream[Solution] = {
    recursiveSolver(sortPieces(pieces, board))
  }

}
