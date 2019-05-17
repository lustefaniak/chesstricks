package chesstricks

import utest._
import scala.collection.immutable.BitSet
import Piece._

object PieceCombinatorTest extends TestSuite {

  val smallBoard = Board(3, 3)

  val tests = TestSuite {

    "return X*Y combinations of one piece on X x Y board" - {
      assert(PieceCombinator(King, 1, smallBoard).combine().size == smallBoard.numberOfFields)
    }

    "return none if no pieces to put" - {
      assert(PieceCombinator(King, 0, smallBoard).combine().size == 0)
    }

    "return none if impossible" - {
      assert(PieceCombinator(King, 9, smallBoard).combine().size == 0)
    }

    "return one if only one possible" - {
      assert(PieceCombinator(King, 4, smallBoard).combine().size == 1)
    }

    "work well with Rock" - {
      assert(PieceCombinator(Rook, 1, smallBoard).combine().size == 9)
      assert(PieceCombinator(Rook, 2, smallBoard).combine().size == 18)
      assert(PieceCombinator(Rook, 3, smallBoard).combine().size == 6)
      assert(PieceCombinator(Rook, 4, smallBoard).combine().size == 0)
    }

    "work well with Bishop" - {
      assert(PieceCombinator(Bishop, 1, smallBoard).combine().size == 9)
      assert(PieceCombinator(Bishop, 4, smallBoard).combine().size == 8)
    }

    "work well with Knight" - {
      assert(PieceCombinator(Knight, 1, smallBoard).combine().size == 9)
      assert(PieceCombinator(Knight, 5, smallBoard).combine().size == 2)
      assert(PieceCombinator(Knight, 6, smallBoard).combine().size == 0)
    }

    "work well with Queen" - {
      assert(PieceCombinator(Queen, 1, smallBoard).combine().size == 9)
      assert(PieceCombinator(Queen, 2, smallBoard).combine().size == 8)
      //    PieceCombinator(Queen, 2, smallBoard).combinationsOfPlaces().map(c=> Map[Piece,BitSet](Queen -> c)).foreach{
      //      r=>
      //      smallBoard.prettyPrint(r)
      //    }
    }

    "not return duplicates" - {
      val combinations = PieceCombinator(King, 4, smallBoard).combine()
      assert(combinations == combinations.distinct)
    }

    "combineReturningAvailablePlaces should  return less available places as a result" - {
      val allAreAvailable = smallBoard.bitsetWithAllFields
      PieceCombinator(King, 2, smallBoard).combineReturningAvailablePlaces(allAreAvailable) foreach {
        case PieceCombinator.Combination(_, availablePlaces) =>
          assert(availablePlaces.size < allAreAvailable.size)
      }
    }

    "combineReturningAvailablePlaces should  never place new piece on already used position" - {
      val alreadyPlaced = BitSet(0, 1, 2)
      PieceCombinator(King, 1, smallBoard).combineReturningAvailablePlaces(alreadyPlacedPieces = alreadyPlaced) foreach {
        case PieceCombinator.Combination(positions, _) =>
          assert((positions & alreadyPlaced).size == 0)
      }
    }

    "combineReturningAvailablePlaces should mark as unavailable places with already placed pieces" - {
      val alreadyPlaced = BitSet(0, 1, 2)
      PieceCombinator(King, 1, smallBoard).combineReturningAvailablePlaces(alreadyPlacedPieces = alreadyPlaced) foreach {
        case PieceCombinator.Combination(_, available) =>
          assert((available & alreadyPlaced).size == alreadyPlaced.size)
      }
    }

  }

}
