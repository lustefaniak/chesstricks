import scala.collection.immutable.BitSet

object PieceCombinator {

  case class Combination(positionsPlaced: BitSet, availablePlaces: BitSet)

}

case class PieceCombinator(piece: Piece, numberOf: Int, implicit val board: Board) {

  import PieceCombinator._

  lazy val captures = board.pieceCaptures(piece)

  def recursiveCombinations(piecesLeftToPut: Int, availablePlaces: BitSet, alreadyPlacedPieces: BitSet = BitSet(), resultAccumulator: BitSet = BitSet()): Stream[BitSet] = {
    if (piecesLeftToPut == 0) {
      //FIXME: we could return availablePlaces from here, but that would require additional parameter passing or better earlier recursion breaking
      Stream(resultAccumulator)
    } else {
      (availablePlaces &~ alreadyPlacedPieces).toStream.flatMap {
        bit =>
          val alreadyPlacedIntersections = alreadyPlacedPieces & captures(bit)
          if (alreadyPlacedIntersections.isEmpty) {
            val newAvailablePlaces = (availablePlaces &~ captures(bit)) filter (_ > bit)
            recursiveCombinations(piecesLeftToPut - 1, newAvailablePlaces, alreadyPlacedPieces + bit, resultAccumulator + bit)
          } else {
            Stream.empty
          }
      }
    }
  }

  def combine(availablePlaces: BitSet = board.bitsetWithAllFields, alreadyPlacedPieces: BitSet = BitSet()): Stream[BitSet] = {
    if (numberOf <= 0) {
      Stream.empty
    } else {
      recursiveCombinations(numberOf, availablePlaces, alreadyPlacedPieces)
    }
  }

  def combineReturningAvailablePlaces(availablePlaces: BitSet = board.bitsetWithAllFields, alreadyPlacedPieces: BitSet = BitSet()): Stream[Combination] = {
    combine(availablePlaces, alreadyPlacedPieces).map {
      case positions: BitSet =>
        var newAvailable: BitSet = availablePlaces &~ positions
        positions.foreach(i => newAvailable = newAvailable &~ captures(i))
        Combination(positions, newAvailable)
    }
  }

}
