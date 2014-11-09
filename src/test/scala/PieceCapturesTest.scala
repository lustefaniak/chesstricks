import org.scalatest.{FlatSpec, Matchers}


class PieceCapturesTest extends FlatSpec with Matchers {
  import Piece._

  implicit val board = Board.default

  "PieceCaptures" should "create X*Y Bitmaps" in {

    PieceCaptures.generate(King) should have size (board.X * board.Y)

  }

  Pieces.get foreach {
    piece=>
      it should s"work for ${piece.getClass} piece" in {
        PieceCaptures.generate(piece) should have size (board.X * board.Y)
    }
  }
}
