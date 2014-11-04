import org.scalatest.{Matchers, FlatSpec}

import scala.collection.BitSet


class PieceTest extends FlatSpec with Matchers {
  import Piece._

  implicit val board = Board(8,8)

  "movedInAllDirectionsByOneField" should "move to all the fields in 3x3 from center" in {
    implicit val smallerBoard = Board(3,3)
    movedInAllDirectionsByOneField(Position(1,1))(smallerBoard) should have size (8)
  }

  it should "not move any further" in {
    implicit val smallerBoard = Board(5,5)
    movedInAllDirectionsByOneField(Position(2,2))(smallerBoard) should have size (8)
  }


  it should "move to 3 fields if in corner" in {
    implicit val smallerBoard = Board(3,3)
    movedInAllDirectionsByOneField(Position(0,0))(smallerBoard) should have size (3)
    movedInAllDirectionsByOneField(Position(2,2))(smallerBoard) should have size (3)
    movedInAllDirectionsByOneField(Position(0,2))(smallerBoard) should have size (3)
    movedInAllDirectionsByOneField(Position(2,0))(smallerBoard) should have size (3)
  }

  "movesVertically" should "change only y" in {
    val position = Position(1,1)
    val moves = movesVertically(position)
    moves should have size(board.X-1)
    moves.foreach {
      p =>
        p.x should be (position.x)
    }
  }

  it should "generate distinct moves" in {
    val position = Position(1,1)
    (movesVertically(position) distinct) should be (movesVertically(position))
  }

  "movesHorizontally" should "change only x" in {
    val position = Position(1,1)
    val moves = movesHorizontally(position)
    moves should have size(board.Y-1)
    moves.foreach {
      p =>
        p.y should be (position.y)
    }
  }

  it should "generate distinct moves" in {
    val position = Position(1,1)
    (movesHorizontally(position) distinct) should be (movesHorizontally(position))
  }

  "movesDiagonally" should "move from 0,0 to n,n" in {
    val position = Position(0,0)
    val maxBoardSize = Math.min(board.X, board.Y) - 1
    val endPosition = Position(maxBoardSize,maxBoardSize)

    movesDiagonally(position) should contain (endPosition)
  }

  it should "also move in opsite direction" in {
    val position = Position(0,0)
    val maxBoardSize = Math.min(board.X, board.Y) - 1
    val endPosition = Position(maxBoardSize,maxBoardSize)

    movesDiagonally(endPosition) should contain (position)
  }

  it should "work for other diagonal too" in {
    val position = Position(board.X - 1, 0)
    val endPosition = Position(0, board.Y - 1)

    movesDiagonally(position) should contain (endPosition)

  }

  it should "move oposite direction on other diagonal" in {
    val position = Position(board.X - 1, 0)
    val endPosition = Position(0, board.Y - 1)

    movesDiagonally(endPosition) should contain (position)
  }

  it should "move from the center in both diagonals on 3x3" in {
    implicit val smallerBoard = Board(3,3)
    val moves = movesDiagonally(Position(1,1))(smallerBoard)

    moves should have size (4)

  }

  it should "move from the center in both diagonals on 5x5" in {
    implicit val smallerBoard = Board(5,5)
    val moves = movesDiagonally(Position(2,2))(smallerBoard)

    moves should have size (8)

  }

  "Queen moves" should "be same as kings on 3x3 board" in {
    val queen = Queen
    val king = King
    val board = Board(3, 3)
    val position = Position(1,1)
    queen.possibleMoves(position)(board) should equal (king.possibleMoves(position)(board))
  }

  it should "be move by one in every direction" in {
    val queen = Queen
    val king = King
    val board = Board(3, 3)
    val position = Position(1,1)
    queen.possibleMoves(position)(board) should equal (Piece.movedInAllDirectionsByOneField(position)(board))
  }

  "Board" should "properly translate Position to bit on 3x3" in {
    val board = Board(3,3)
    board.positionToBit(Position(0,0)) should be (0)
    board.positionToBit(Position(1,1)) should be (4)
    board.positionToBit(Position(2,2)) should be (8)
  }

  it should "encode bits for small board 3x3" in {
    val board = Board(3,3)
    val positions = Seq(Position(0,0),Position(1,1),Position(2,2))

    val encoded = board.encodePositionsInBitset(positions)

    encoded.get(0) should be (true)
    encoded.get(4) should be (true)
    encoded.get(8) should be (true)

  }



}
