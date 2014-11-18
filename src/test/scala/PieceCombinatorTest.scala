import org.scalatest.{Matchers, FlatSpec, FunSuite}

import scala.collection.immutable.BitSet


class PieceCombinatorTest extends FlatSpec with Matchers {

  val smallBoard = Board(3,3)

  "combine" should "return X*Y combinations of one piece on X x Y board" in {
    PieceCombinator(King, 1, smallBoard).combine() should have size (smallBoard.numberOfFields)
  }

  it should "return none if no pieces to put" in {
    PieceCombinator(King, 0, smallBoard).combine() should have size (0)
  }

  it should "return none if impossible" in {
    PieceCombinator(King, 9, smallBoard).combine() should have size (0)
  }

  it should "return one if only one possible" in {
    PieceCombinator(King, 4, smallBoard).combine() should have size (1)
  }

  it should "work well with Rock" in {
    PieceCombinator(Rook, 1, smallBoard).combine() should have size (9)
    PieceCombinator(Rook, 2, smallBoard).combine() should have size (18)
    PieceCombinator(Rook, 3, smallBoard).combine() should have size (6)
    PieceCombinator(Rook, 4, smallBoard).combine() should have size (0)
  }

  it should "work well with Bishop" in {
    PieceCombinator(Bishop, 1, smallBoard).combine() should have size (9)
    PieceCombinator(Bishop, 4, smallBoard).combine() should have size (8)
  }

  it should "work well with Knight" in {
    PieceCombinator(Knight, 1, smallBoard).combine() should have size (9)
    PieceCombinator(Knight, 5, smallBoard).combine() should have size (2)
    PieceCombinator(Knight, 6, smallBoard).combine() should have size (0)
  }

  it should "work well with Queen" in {
    PieceCombinator(Queen, 1, smallBoard).combine() should have size (9)
    PieceCombinator(Queen, 2, smallBoard).combine() should have size (8)
//    PieceCombinator(Queen, 2, smallBoard).combinationsOfPlaces().map(c=> Map[Piece,BitSet](Queen -> c)).foreach{
//      r=>
//      smallBoard.prettyPrint(r)
//    }
  }

  it should "not return duplicates" in {
    val combinations = PieceCombinator(King, 4, smallBoard).combine()
    combinations should equal (combinations.distinct)
  }

  "combineReturningAvailablePlaces" should "return less available places as a result" in {
    val allAreAvailable = smallBoard.bitsetWithAllFields
    PieceCombinator(King, 2, smallBoard).combineReturningAvailablePlaces(allAreAvailable) foreach {
      case PieceCombinator.Combination(_, availablePlaces) =>
        (availablePlaces.size) should be < (allAreAvailable.size)
    }
  }

  it should "never place new piece on already used position" in {
    val alreadyPlaced = BitSet(0,1,2)
    PieceCombinator(King, 1, smallBoard).combineReturningAvailablePlaces(alreadyPlacedPieces = alreadyPlaced) foreach {
      case PieceCombinator.Combination(positions, _) =>
        (positions & alreadyPlaced) should have size (0)
    }
  }

  it should "mark as unavailable places with already placed pieces" in {
    val alreadyPlaced = BitSet(0,1,2)
    PieceCombinator(King, 1, smallBoard).combineReturningAvailablePlaces(alreadyPlacedPieces = alreadyPlaced) foreach {
      case PieceCombinator.Combination(_, available) =>
        (available & alreadyPlaced) should have size (alreadyPlaced.size)
    }
  }


}
