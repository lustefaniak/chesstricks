import scala.collection.immutable.BitSet

object Position {

  implicit def ordering[A <: Position]: Ordering[A] = Ordering.by(e => (e.x, e.y))

}

case class Position(x: Int, y: Int)

case class Move(deltaX: Int, deltaY: Int)

object Board {
  def default = Board(8, 8)
}

case class Board(X: Int, Y: Int) extends Output {
  require(X > 0 && Y > 0)

  lazy val numberOfFields = X * Y

  lazy val bitsetWithAllFields = BitSet((0 until numberOfFields).toSeq: _*)

  def mapToValidPosition(position: Position): Option[Position] = {
    if (position.x >= 0 && position.x < X && position.y >= 0 && position.y < Y)
      Some(position)
    else None
  }

  def applyMoveToPosition(move: Move, currentPosition: Position): Option[Position] =
    Option(currentPosition.copy(x = currentPosition.x + move.deltaX, currentPosition.y + move.deltaY)).filter {
      case p =>
        p.x >= 0 && p.x < X && p.y >= 0 && p.y < Y
    }

  @inline
  def positionToBit(position: Position): Option[Int] = mapToValidPosition(position).map(pos => pos.y * X + pos.x)

  @inline
  def bitToPosition(bit: Int): Option[Position] = mapToValidPosition(Position(bit % X, Math.floor(bit / X).toInt))

  def encodePositionsInBitset(positions: Seq[Position]): BitSet = BitSet(positions.flatMap(positionToBit).toSeq: _*)

  def prettyBoardGen(combinations: Map[Piece, BitSet]): Seq[String] = {

    val buffer = (" " * numberOfFields).toCharArray

    combinations.foreach {
      case (piece, positions) =>
        positions.filter(p => p >= 0 && p < numberOfFields).foreach {
          pos =>
            buffer(pos) = piece.letter
        }
    }

    "+" + ("-" * X) + "+" :: buffer.sliding(X, X).toSeq.reverse.map(l => "|" + l.mkString + "|").toList ::: "+" + ("-" * X) + "+" :: Nil
  }

  def prettyPrint(combinations: Map[Piece, BitSet]): Unit = {
    output(prettyBoardGen(combinations).mkString("\n"))
  }

  protected val _pieceCaptures = collection.mutable.Map[Piece, Map[Int, BitSet]]()

  def pieceCaptures(piece: Piece) = {
    _pieceCaptures.getOrElseUpdate(piece, PieceCaptures.generate(piece)(this))
  }

}

