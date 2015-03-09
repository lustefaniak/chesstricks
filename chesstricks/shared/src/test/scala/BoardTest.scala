import utest._
import scala.collection.immutable.BitSet


object BoardTest extends TestSuite {

  val tests = TestSuite {
    "encodePositionsInBitset should encode bits for small board 3x3" - {
      val board = Board(3, 3)
      val positions = Seq(Position(0, 0), Position(1, 1), Position(2, 2))

      val encoded = board.encodePositionsInBitset(positions)

      assert(encoded == BitSet(0, 4, 8))

    }

    "positionToBit should properly translate Position to bit on 3x3" - {
      val board = Board(3, 3)
      assert(board.positionToBit(Position(0, 0)) == Some(0))
      assert(board.positionToBit(Position(1, 1)) == Some(4))
      assert(board.positionToBit(Position(2, 2)) == Some(8))
    }

    "mapToValidPosition should properly handle piece on board" - {
      val board = Board(3, 3)
      val position = Position(1, 1)
      assert(board.mapToValidPosition(position) == Some(position))
    }

    "mapToValidPosition should reject wrong positions" - {
      val board = Board(3, 3)
      val wrongPositions = List(Position(0, -1), Position(-1, -1), Position(-1, 0), Position(3, 0), Position(0, 3), Position(3, 3), Position(9999, 9999))
      wrongPositions foreach {
        position =>
          assert(board.mapToValidPosition(position) == None)
      }
    }

    "mapToValidPosition should work well with bitToPosition" - {
      val board = Board.default
      val position = Position(4, 4)
      assert(board.positionToBit(position).flatMap(board.bitToPosition) == Some(position))
    }

    "mapToValidPosition should handle well highest bit" - {
      val board = Board.default
      val position = Position(board.X - 1, board.Y - 1)
      assert(board.positionToBit(position).flatMap(board.bitToPosition) == Some(position))
    }

    "mapToValidPosition should also work for lowest bit" - {
      val board = Board.default
      val position = Position(0, 0)
      assert(board.positionToBit(position).flatMap(board.bitToPosition) == Some(position))
    }

    "prettyBoardGen should generate example visualisation" - {
      val board = Board(3, 3)
      val exampleSolution = Map[Piece, BitSet](King -> BitSet(0, 2), Rook -> BitSet(7))

      val expectedOutput = List("+---+",
        "| R |",
        "|   |",
        "|K K|",
        "+---+")
      assert(board.prettyBoardGen(exampleSolution) == expectedOutput)

    }

    trait MockOutput extends Output {
      var printedLines = List[String]()

      override def output(s: String) {
        printedLines ::= s
      }
    }

    "prettyPrint should generate proper output for example visualisation" - {
      val board = new Board(3, 3) with MockOutput
      val exampleSolution = Map[Piece, BitSet](King -> BitSet(0, 2), Rook -> BitSet(7))
      val expectedOutput = "+---+\n| R |\n|   |\n|K K|\n+---+"
      board.prettyPrint(exampleSolution)

      assert(board.printedLines.contains(expectedOutput))
    }

  }
}
