import scala.collection.immutable.BitSet
import org.scalatest.{FlatSpec, Matchers}


class BoardTest extends FlatSpec with Matchers {

  "encodePositionsInBitset" should "encode bits for small board 3x3" in {
    val board = Board(3,3)
    val positions = Seq(Position(0,0),Position(1,1),Position(2,2))

    val encoded = board.encodePositionsInBitset(positions)

    encoded should be (BitSet(0,4,8))

  }

  "positionToBit" should "properly translate Position to bit on 3x3" in {
    val board = Board(3,3)
    board.positionToBit(Position(0,0)) should be (Some(0))
    board.positionToBit(Position(1,1)) should be (Some(4))
    board.positionToBit(Position(2,2)) should be (Some(8))
  }

  it should "work well with bitToPosition" in {
    val board = Board.default
    val position = Position(4, 4)
    board.positionToBit(position).flatMap(board.bitToPosition) should equal (Some(position))
  }

  it should "handle well highest bit" in {
    val board = Board.default
    val position = Position(board.X - 1 , board.Y - 1)
    board.positionToBit(position).flatMap(board.bitToPosition) should equal(Some(position))
  }

  it should "also work for lowest bit" in {
    val board = Board.default
    val position = Position(0, 0)
    board.positionToBit(position).flatMap(board.bitToPosition) should equal(Some(position))
  }

}
