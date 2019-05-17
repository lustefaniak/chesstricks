package chesstricks

import scala.collection.immutable.BitSet

object PieceCaptures {

  /**
    * Generated cache of captured fields of a piece on board
    *
    * Returns map of bitmaps. Key is piece position encoded as a bit,
    * BitSet value represents what fields will be captured, if Piece
    * is at bit position
    *
    * @param piece
    * @param board
    * @return
    */
  def generate(piece: Piece)(implicit board: Board): Map[Int, BitSet] = {
    val allPossibleFieldsAsBits = 0 until board.numberOfFields

    val capturesFromPositions = for {
      bit      <- allPossibleFieldsAsBits
      position <- board.bitToPosition(bit)
    } yield {
      val capturesOfPiece  = piece.possibleMoves(position)
      val capturesAsBitmap = board.encodePositionsInBitset(capturesOfPiece)
      bit -> capturesAsBitmap
    }

    capturesFromPositions.toMap

  }
}
