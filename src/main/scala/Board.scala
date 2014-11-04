import org.apache.spark.util.collection.BitSet

object Position {

  implicit def ordering[A <: Position]: Ordering[A] = Ordering.by(e => (e.x,e.y))

}

case class Position(x:Int, y:Int)

case class Move(deltaX:Int, deltaY:Int)

object Board {
  def default = Board(8, 8)
}

case class Board(val X:Int, val Y:Int) {
  require(X > 0)
  require(Y > 0)

  def numberOfFields = X*Y

  def mapToValidPosition(position:Position):Option[Position] = {
    if(position.x >= 0 && position.x < X && position.y >= 0 && position.y < Y)
      Some(position)
    else None
  }

  def applyMoveToPosition(move:Move, currentPosition:Position):Option[Position] =
    Option(currentPosition.copy(x = currentPosition.x + move.deltaX, currentPosition.y + move.deltaY)).filter{
      case p=>
        p.x >= 0 && p.x < X && p.y >= 0 && p.y < Y   }

  @inline
  def positionToBit(position:Position) : Option[Int] = mapToValidPosition(position).map(pos=> pos.y * X + pos.x)

  @inline
  def bitToPosition(bit:Int) : Option[Position] =  mapToValidPosition(Position(bit % X, Math.floor(bit / X).toInt))

  def encodePositionsInBitset(positions:Seq[Position]):BitSet = {
    val bitset = new BitSet(X*Y)
    val bits = positions.flatMap(positionToBit).foreach(bitset.set(_))
    bitset
  }
}

object Piece {
  def movesHorizontally(position:Position)(implicit board:Board) : Seq[Position] = for {
    x <- 0 until board.X if x != position.x
  } yield Position(x, position.y)

  def movesVertically(position:Position)(implicit board:Board) : Seq[Position] = for {
    y <- 0 until board.Y if y != position.y
  } yield Position(position.x, y)

  def movedInAllDirectionsByOneField(position:Position)(implicit board:Board) : Seq[Position] = {
    val moves = for {
      x <- (-1 to 1 by 1)
      y <- (-1 to 1 by 1)
    } yield Move(x,y)

    moves.flatMap(board.applyMoveToPosition(_, position)).filter( _ != position)
  }

  def movesDiagonally(position:Position)(implicit board:Board) : Seq[Position] = {
    //FIXME: waste of time, think about boundries
    val diagonal1 = (for {
      i <- -board.X until board.X if i != 0
    } yield Position(position.x + i, position.y + i))

    val diagonal2 = (for {
      i <- -board.Y until board.Y if i != 0
    } yield Position(position.x + i, position.y - i))
    //FIXME: waste of time, think about boundries
    val result = (diagonal1 ++ diagonal2) filter {
        p=> p.x >= 0 && p.x < board.X && p.y >= 0 && p.y < board.Y
      } distinct

    result
  }

  def movesAlongRankAndFile(position:Position)(implicit board:Board) : Seq[Position] =
    movesHorizontally(position) ++ movesVertically(position)
}

sealed trait Piece {
  def possibleMoves(position:Position)(implicit board:Board) : Seq[Position]
  def estimateAverageCapturedFields(numberOf:Int)(implicit board:Board) : Int
}


object Queen extends Piece {
  override def possibleMoves(position: Position)(implicit board: Board): Seq[Position] =
    Piece.movesAlongRankAndFile(position)(board) ++ Piece.movesDiagonally(position)(board)


  override def estimateAverageCapturedFields(numberOf: Int)(implicit board: Board): Int = (numberOf * 4 * (board.X + board.Y) / 2) toInt
}

object King extends Piece {
  override def possibleMoves(position: Position)(implicit board: Board): Seq[Position] = Piece.movedInAllDirectionsByOneField(position)(board)

  override def estimateAverageCapturedFields(numberOf: Int)(implicit board: Board): Int = numberOf * 7
}
object Bishop extends Piece{
  override def possibleMoves(position: Position)(implicit board: Board): Seq[Position] = Piece.movesDiagonally(position)(board)

  override def estimateAverageCapturedFields(numberOf: Int)(implicit board: Board): Int = (numberOf * 2 * (board.X + board.Y - 2) / 2) toInt
}
object Rock extends Piece {
  override def possibleMoves(position: Position)(implicit board: Board): Seq[Position] = Piece.movesAlongRankAndFile(position)(board)

  override def estimateAverageCapturedFields(numberOf: Int)(implicit board: Board): Int = numberOf * (board.X + board.Y - 2)
}

object Knight extends Piece {
  override def possibleMoves(position: Position)(implicit board: Board): Seq[Position] = {

    val moves = Seq(
      Move(-1,2),
      Move(1,2),
      Move(2,-1),
      Move(2,1),
      Move(1,-2),
      Move(-1,-2),
      Move(-2, -1),
      Move(-2,1)
    )

    moves.flatMap(board.applyMoveToPosition(_,position))

  }

  override def estimateAverageCapturedFields(numberOf: Int)(implicit board: Board): Int = numberOf * 8
}

object Pieces {
  def get = List(Queen, Bishop, Rock, King, Knight)
}


