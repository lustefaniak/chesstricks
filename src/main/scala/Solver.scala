import scala.collection.immutable.BitSet

object Solver {

  case class Solution(combinations: Map[Piece, BitSet])

}

case class Solver(val board: Board) {

  import Solver._

  def recursiveSolver(piecesLeftToPut: List[(Piece, Int)], availablePlaces: BitSet = board.bitsetWithAllFields, alreadyPlacedPieces: BitSet = BitSet(), solutionAccumulator: Map[Piece, BitSet] = Map()): Stream[Solution] = piecesLeftToPut match {

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

  def sortPieces(pieces: Map[Piece, Int]) = pieces.toList.sortBy {
    case (piece, numberOf) =>
      val estimate = piece.estimateAverageCapturedFields(numberOf)(board)
      //println(s"$piece $numberOf $estimate")
      -estimate
  }

  def solve(pieces: Map[Piece, Int]): Stream[Solution] = {
    recursiveSolver(sortPieces(pieces))
  }

}
