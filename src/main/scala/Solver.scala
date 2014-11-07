import scala.collection.immutable.BitSet

object Solver {
  case class Solution(combinations:Map[Piece,BitSet])
}

case class Solver(val board:Board) {

  import Solver._

  def recursiveSolver(piecesLeftToPut:List[(Piece,Int)], availablePlaces:BitSet = board.bitsetWithAllFields, alreadyPlacedPieces:BitSet = BitSet(), solutionAccumulator:Map[Piece,BitSet] = Map()): Stream[Solution] = piecesLeftToPut match {

      case (piece,numberOf) :: rest =>

        PieceCombinator(piece, numberOf, board).combinationsOfPlacesWithAvailables(availablePlaces, alreadyPlacedPieces).flatMap {
          case PieceCombinator.Combination(positionsPlaces, availablePlaces) =>

            val placesWithPiece = alreadyPlacedPieces | positionsPlaces

            recursiveSolver(rest, availablePlaces, placesWithPiece, solutionAccumulator + (piece -> positionsPlaces))
        }

      case Nil =>
        if(solutionAccumulator.isEmpty) Stream.empty else Stream(Solution(solutionAccumulator))

    }

  def solve(pieces:Map[Piece,Int]) : Stream[Solution] = {

    val piecesOrderedByEstimatedCoverage = pieces.toList.sortBy{
      case (piece, numberOf) => -piece.estimateAverageCapturedFields(numberOf)(board)
    }

    recursiveSolver(piecesOrderedByEstimatedCoverage)

  }

}
