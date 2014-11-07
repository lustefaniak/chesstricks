import org.scalatest.{Matchers, FlatSpec, FunSuite}


class PieceCombinatorTest extends FlatSpec with Matchers {

  val smallBoard = Board(3,3)

  "combinations" should "return X*Y combinations of one piece on X x Y board" in {
    PieceCombinator(King, 1, smallBoard).combinationsOfPlaces() should have size (smallBoard.numberOfFields)
  }

  it should "return none if no pieces to put" in {
    PieceCombinator(King, 0, smallBoard).combinationsOfPlaces() should have size (0)
  }

  it should "return none if impossible" in {
    PieceCombinator(King, 9, smallBoard).combinationsOfPlaces() should have size (0)
  }

  it should "return one if only one possible" in {
    PieceCombinator(King, 4, smallBoard).combinationsOfPlaces() should have size (1)
  }

  it should "work well with Rock" in {
    PieceCombinator(Rock, 1, smallBoard).combinationsOfPlaces() should have size (9)
    PieceCombinator(Rock, 2, smallBoard).combinationsOfPlaces() should have size (18)
    PieceCombinator(Rock, 3, smallBoard).combinationsOfPlaces() should have size (6)
    PieceCombinator(Rock, 4, smallBoard).combinationsOfPlaces() should have size (0)
  }

  it should "work well with Bishop" in {
    PieceCombinator(Bishop, 1, smallBoard).combinationsOfPlaces() should have size (9)
    PieceCombinator(Bishop, 4, smallBoard).combinationsOfPlaces() should have size (8)
  }

  it should "work well with Knight" in {
    PieceCombinator(Knight, 1, smallBoard).combinationsOfPlaces() should have size (9)
    PieceCombinator(Knight, 5, smallBoard).combinationsOfPlaces() should have size (2)
    PieceCombinator(Knight, 6, smallBoard).combinationsOfPlaces() should have size (0)
  }

  it should "not return duplicates" in {
    val combinations = PieceCombinator(King, 4, smallBoard).combinationsOfPlaces()
    combinations should equal (combinations.distinct)
  }

  "bitsetWithAllFields" should "have proper number of elements" in {
    PieceCombinator(King,1, smallBoard).bitsetWithAllFields should have size (smallBoard.numberOfFields)
  }

}
