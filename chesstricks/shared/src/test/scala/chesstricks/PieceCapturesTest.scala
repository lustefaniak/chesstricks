package chesstricks

import utest._
import Piece._

object PieceCapturesTest extends TestSuite {

  implicit val board = Board.default

  val tests = TestSuite {
    'work_for_Queen {
      assert(PieceCaptures.generate(Queen).size == (board.X * board.Y))
    }

    'work_for_Bishop {
      assert(PieceCaptures.generate(Bishop).size == (board.X * board.Y))
    }

    'work_for_Rook {
      assert(PieceCaptures.generate(Rook).size == (board.X * board.Y))
    }

    'work_for_King {
      assert(PieceCaptures.generate(King).size == (board.X * board.Y))
    }

    'work_for_Knight {
      assert(PieceCaptures.generate(Knight).size == (board.X * board.Y))
    }
  }

}
