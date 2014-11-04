import scala.collection.immutable.BitSet

case class PieceCaptures(piece:Piece)(implicit board:Board) {

  def generateCaptures: Map[Int, BitSet] = {
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

  def from(position:Position) = board.positionToBit(position).flatMap(b=>captures.get(b))

  def from(bit:Int) = captures.get(bit)

  lazy val captures: Map[Int,BitSet] = generateCaptures

}
