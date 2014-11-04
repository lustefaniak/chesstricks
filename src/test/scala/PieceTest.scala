import org.scalatest.{Matchers, FlatSpec}

import scala.collection.BitSet


class PieceTest extends FlatSpec with Matchers {
  import Piece._

  implicit val board = Board.default

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
    (movesHorizontally(position)) should be (movesHorizontally(position) distinct)
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
    //FIXME: change macher to the one ignoring order or do diff?
    queen.possibleMoves(position)(board).sorted should equal (king.possibleMoves(position)(board).sorted)
  }

  it should "be move by one in every direction" in {
    val queen = Queen
    val king = King
    val board = Board(3, 3)
    val position = Position(1,1)
    //FIXME: change macher to the one ignoring order or do diff?
    queen.possibleMoves(position)(board).sorted should equal (Piece.movedInAllDirectionsByOneField(position)(board).sorted)
  }


  "Knight moves" should "at least have correct number" in {
    val knight = Knight
    val board = Board(5,5)
    val position = Position(2,2)
    knight.possibleMoves(position)(board) should have size (8)

  }

}
