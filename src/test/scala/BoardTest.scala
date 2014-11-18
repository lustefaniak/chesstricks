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

  "mapToValidPosition" should "properly handle piece on board" in {
    val board = Board(3,3)
    val position = Position(1,1)
    board.mapToValidPosition(position) should be (Some(position))
  }

  it should "reject wrong positions" in {
    val board = Board(3,3)
    val wrongPositions = List(Position(0,-1), Position(-1,-1), Position(-1,0), Position(3,0), Position(0,3), Position(3,3), Position(9999,9999))
    wrongPositions foreach {
      position =>
      board.mapToValidPosition(position) should be(None)
    }
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

  "prettyBoardGen" should "generate example visualisation" in {
    val board = Board(3,3)
    val exampleSolution = Map[Piece,BitSet](King -> BitSet(0, 2), Rook -> BitSet(7))

    val expectedOutput = List("+---+",
                              "| R |",
                              "|   |",
                              "|K K|",
                              "+---+")
    board.prettyBoardGen(exampleSolution) should be (expectedOutput)

  }

  trait MockOutput extends Output {
    var printedLines = List[String]()

    override def output(s: String) {
      printedLines ::= s
    }
  }

  "prettyPrint" should "generate proper output for example visualisation" in {
    val board = new Board(3,3) with MockOutput
    val exampleSolution = Map[Piece,BitSet](King -> BitSet(0, 2), Rook -> BitSet(7))
    val expectedOutput = "+---+\n| R |\n|   |\n|K K|\n+---+"
    board.prettyPrint(exampleSolution)

    board.printedLines should contain (expectedOutput)
  }

}
