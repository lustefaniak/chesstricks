
object Piece {
  def movesHorizontally(position: Position)(implicit board: Board): Seq[Position] = for {
    x <- 0 until board.X if x != position.x
  } yield Position(x, position.y)

  def movesVertically(position: Position)(implicit board: Board): Seq[Position] = for {
    y <- 0 until board.Y if y != position.y
  } yield Position(position.x, y)

  def movedInAllDirectionsByOneField(position: Position)(implicit board: Board): Seq[Position] = {
    val moves = for {
      x <- (-1 to 1 by 1)
      y <- (-1 to 1 by 1)
    } yield Move(x, y)

    moves.flatMap(board.applyMoveToPosition(_, position)).filter(_ != position)
  }

  def movesDiagonally(position: Position)(implicit board: Board): Seq[Position] = {
    //FIXME: waste of time, think about boundries
    val diagonal1 = (for {
      i <- -board.X until board.X if i != 0
    } yield Position(position.x + i, position.y + i))

    val diagonal2 = (for {
      i <- -board.Y until board.Y if i != 0
    } yield Position(position.x + i, position.y - i))
    //FIXME: waste of time, think about boundries
    val result = (diagonal1 ++ diagonal2) filter {
      p => p.x >= 0 && p.x < board.X && p.y >= 0 && p.y < board.Y
    } distinct

    result
  }

  def movesAlongRankAndFile(position: Position)(implicit board: Board): Seq[Position] =
    movesHorizontally(position) ++ movesVertically(position)
}

sealed trait Piece {
  val letter: Char = this.getClass.getName.head

  def possibleMoves(position: Position)(implicit board: Board): Seq[Position]

  def estimateAverageCapturedFields(numberOf: Int)(implicit board: Board): Int
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

object Bishop extends Piece {
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
      Move(-1, 2),
      Move(1, 2),
      Move(2, -1),
      Move(2, 1),
      Move(1, -2),
      Move(-1, -2),
      Move(-2, -1),
      Move(-2, 1)
    )

    moves.flatMap(board.applyMoveToPosition(_, position))

  }

  override val letter: Char = 'k'

  override def estimateAverageCapturedFields(numberOf: Int)(implicit board: Board): Int = numberOf * 8
}

object Pieces {
  def get = List(Queen, Bishop, Rock, King, Knight)
}

