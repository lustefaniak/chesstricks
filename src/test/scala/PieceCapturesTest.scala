import org.scalatest.{FlatSpec, Matchers}


class PieceCapturesTest extends FlatSpec with Matchers {
  import Piece._

  implicit val board = Board.default

  "PieceCaptures" should "create X*Y Bitmaps" in {

    PieceCaptures(King).captures should have size (board.X * board.Y)

  }


  Pieces.get foreach {
    piece=>
      it should s"work for ${piece.getClass} piece" in {
        PieceCaptures(piece).captures should have size (board.X * board.Y)
    }
  }
}
