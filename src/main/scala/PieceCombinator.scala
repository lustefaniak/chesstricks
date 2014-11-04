import scala.collection.immutable.BitSet

case class PieceCombinator(piece:Piece, numberOf:Int)(implicit val board:Board) {

  lazy val captures = PieceCaptures(piece).captures

  def bitsetWithAllFields = BitSet((0 until board.numberOfFields).toSeq: _*)

  def recursiveCombinations(piecesLeftToPut:Int, availablePlaces:BitSet=bitsetWithAllFields, alreadyPlacedPieces:BitSet = BitSet()):Stream[BitSet] = {
    if ( piecesLeftToPut == 0 ) {
      Stream(alreadyPlacedPieces)
    } else {
      availablePlaces.toStream.flatMap {
        bit =>
          val capturesFromHere = captures(bit)
          val intersectedPieces = alreadyPlacedPieces & capturesFromHere
          val newAvailablePlaces = (availablePlaces &~ capturesFromHere) filter (_ > bit)
          if (!intersectedPieces.isEmpty) {
            Stream.empty
          } else {
            recursiveCombinations(piecesLeftToPut - 1, newAvailablePlaces, alreadyPlacedPieces + bit)
          }
      }
    }
  }

  def combinations : Stream[BitSet] = {
    if(numberOf <= 0 ) Stream.empty else {
      val available = bitsetWithAllFields
      val foundCombinations = recursiveCombinations(numberOf)
      foundCombinations
    }
  }



}
