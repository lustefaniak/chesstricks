package chesstricks

import utest._

object PieceTest extends TestSuite {

  import Piece._

  implicit val board = Board.default
  val tests = TestSuite {

    "movedInAllDirectionsByOneField should move to all the fields in 3x3 from center" - {
      implicit val smallerBoard = Board(3, 3)
      assert(movedInAllDirectionsByOneField(Position(1, 1))(smallerBoard).size == 8)
    }

    "movedInAllDirectionsByOneField should not move any further" - {
      implicit val smallerBoard = Board(5, 5)
      assert(movedInAllDirectionsByOneField(Position(2, 2))(smallerBoard).size == 8)
    }

    "movedInAllDirectionsByOneField should move to 3 fields if in corner" - {
      implicit val smallerBoard = Board(3, 3)
      assert(movedInAllDirectionsByOneField(Position(0, 0))(smallerBoard).size == 3)
      assert(movedInAllDirectionsByOneField(Position(2, 2))(smallerBoard).size == 3)
      assert(movedInAllDirectionsByOneField(Position(0, 2))(smallerBoard).size == 3)
      assert(movedInAllDirectionsByOneField(Position(2, 0))(smallerBoard).size == 3)
    }

    "movesVertically should change only y" - {
      val position = Position(1, 1)
      val moves    = movesVertically(position)
      assert(moves.size == board.X - 1)
      moves.foreach { p =>
        assert(p.x == position.x)
      }
    }

    "movesVertically should generate distinct moves" - {
      val position = Position(1, 1)
      assert(movesVertically(position).distinct == movesVertically(position))
    }

    "movesHorizontally should change only x" - {
      val position = Position(1, 1)
      val moves    = movesHorizontally(position)
      assert(moves.size == board.Y - 1)
      moves.foreach { p =>
        assert(p.y == position.y)
      }
    }

    "movesHorizontally should generate distinct moves" - {
      val position = Position(1, 1)
      assert(movesHorizontally(position) == movesHorizontally(position).distinct)
    }

    "movesDiagonally should move from 0,0 to n,n" - {
      val position     = Position(0, 0)
      val maxBoardSize = Math.min(board.X, board.Y) - 1
      val endPosition  = Position(maxBoardSize, maxBoardSize)

      assert(movesDiagonally(position) contains endPosition)
    }

    "movesDiagonally should also move in oposite direction" - {
      val position     = Position(0, 0)
      val maxBoardSize = Math.min(board.X, board.Y) - 1
      val endPosition  = Position(maxBoardSize, maxBoardSize)

      assert(movesDiagonally(endPosition) contains position)
    }

    "movesDiagonally should work for other diagonal too" - {
      val position    = Position(board.X - 1, 0)
      val endPosition = Position(0, board.Y - 1)

      assert(movesDiagonally(position) contains endPosition)

    }

    "movesDiagonally should move oposite direction on other diagonal" - {
      val position    = Position(board.X - 1, 0)
      val endPosition = Position(0, board.Y - 1)

      assert(movesDiagonally(endPosition) contains position)
    }

    "movesDiagonally should move from the center in both diagonals on 3x3" - {
      implicit val smallerBoard = Board(3, 3)
      val moves                 = movesDiagonally(Position(1, 1))(smallerBoard)

      assert(moves.size == 4)

    }

    "movesDiagonally should move from the center in both diagonals on 5x5" - {
      implicit val smallerBoard = Board(5, 5)
      val moves                 = movesDiagonally(Position(2, 2))(smallerBoard)

      assert(moves.size == 8)

    }

    "Queen moves should be same as kings on 3x3 board" - {
      val queen    = Queen
      val king     = King
      val board    = Board(3, 3)
      val position = Position(1, 1)

      assert(queen.possibleMoves(position)(board).sorted == king.possibleMoves(position)(board).sorted)
    }

    "Queen moves should be move by one in every direction" - {
      val queen    = Queen
      val board    = Board(3, 3)
      val position = Position(1, 1)
      assert(queen.possibleMoves(position)(board).sorted == Piece.movedInAllDirectionsByOneField(position)(board).sorted)
    }

    "Knight moves should at least have correct number" - {
      val knight   = Knight
      val board    = Board(5, 5)
      val position = Position(2, 2)
      assert(knight.possibleMoves(position)(board).size == 8)
    }

  }
}
