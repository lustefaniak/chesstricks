import scala.collection.immutable.BitSet

object PieceCaptures {

  def generate(piece: Piece)(implicit board: Board): Map[Int, BitSet] = {
    val allPossibleFieldsAsBits = 0 until board.numberOfFields

    val capturesFromPositions = for {
      bit <- allPossibleFieldsAsBits
      position <- board.bitToPosition(bit)
    } yield {
      val capturesOfPiece = piece.possibleMoves(position)
      val capturesAsBitmap = board.encodePositionsInBitset(capturesOfPiece)
      bit -> capturesAsBitmap
    }

    capturesFromPositions.toMap

  }
}
