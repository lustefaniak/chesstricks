import scala.collection.immutable.BitSet

object PieceCombinator {

  case class Combination(positionsPlaced:BitSet, availablePlaces:BitSet)

}

case class PieceCombinator(piece:Piece, numberOf:Int, implicit val board:Board) {
  import PieceCombinator._

  lazy val captures = PieceCaptures(piece)(board).captures

  def bitsetWithAllFields:BitSet = board.bitsetWithAllFields

  def recursiveCombinations(piecesLeftToPut:Int, availablePlaces:BitSet, alreadyPlacedPieces:BitSet = BitSet(), resultAccumulator:BitSet = BitSet()):Stream[BitSet] = {
    if ( piecesLeftToPut == 0 ) {
      //FIXME: we should return availablePlaces from here, but that would require additional parameter or better earlier recursion breaking
      Stream(resultAccumulator)
    } else {
      (availablePlaces &~ alreadyPlacedPieces).toStream.flatMap {
        bit =>
          val capturesFromHere = captures(bit)

          val intersectedPieces = alreadyPlacedPieces & capturesFromHere
          if (intersectedPieces.isEmpty) {
            val newAvailablePlaces = (availablePlaces &~ capturesFromHere) filter (_ > bit)
            recursiveCombinations(piecesLeftToPut - 1, newAvailablePlaces, alreadyPlacedPieces + bit, resultAccumulator + bit)

          } else {
            Stream.empty
          }
      }
    }
  }

  def combinationsOfPlaces(availablePlaces:BitSet = bitsetWithAllFields, alreadyPlacedPieces:BitSet = BitSet()) : Stream[BitSet] = {
    if(numberOf <= 0 ) Stream.empty else {
      recursiveCombinations(numberOf, availablePlaces, alreadyPlacedPieces)
    }
  }

  def combinationsOfPlacesWithAvailables(availablePlaces:BitSet = bitsetWithAllFields, alreadyPlacedPieces:BitSet = BitSet()) : Stream[Combination] = {
    if(numberOf <= 0 ) Stream.empty else {
      val results = recursiveCombinations(numberOf, availablePlaces, alreadyPlacedPieces)

      results.map {
        case positions:BitSet =>
          var available:BitSet = bitsetWithAllFields &~ positions
          positions.foreach(i=> available = available &~ captures(i))

          Combination(positions, available)
      }


    }
  }



}
