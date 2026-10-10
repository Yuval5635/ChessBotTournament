package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

// רגלי: צעד אחד קדימה, שני צעדים מהשורה ההתחלתית, ואוכל באלכסון
public class Pawn extends Piece {

    public Pawn(Color color, int square, Board board, char name) {
        super(color, square, board, name);
    }

    public Pawn(Color color, int square, Board board, char name, boolean hasMoved) {
        super(color, square, board, name, hasMoved);
    }

    // כל המהלכים האפשריים של הכלי. מסמנים true בכל משבצת שאפשר להגיע אליה,
    // ובסוף הופכים את הסימונים למערך של מהלכים.
    @Override
    public Move[] getValidMoves() {
        boolean[] isMoves = new boolean[64];

        // הכיוון קדימה: לבן עולה בשורות (1), שחור יורד (1-)
        int direction = getColor().getValue();

        // צעד אחד קדימה אם המשבצת ריקה. מהשורה ההתחלתית אפשר גם שני צעדים
        if (isValidMove(direction, 0)) {
            isMoves[getSquare() + (direction * 8)] = true;
            if (!hasMoved()) {
                if (isValidMove(direction * 2, 0)) {
                    isMoves[getSquare() + (direction * 16)] = true;
                }
            }
        }

        // אכילה באלכסון, לשני הצדדים
        if (isValidMove(direction, -1)) {
            isMoves[getSquare() + (direction * 8) - 1] = true;
        }

        if (isValidMove(direction, 1)) {
            isMoves[getSquare() + (direction * 8) + 1] = true;
        }

        // סופרים כמה משבצות סומנו, ובונים מהן את מערך המהלכים
        int numOfValidMoves = 0;
        for (boolean isMove : isMoves) {
            if (isMove)
                numOfValidMoves++;
        }

        Move[] validMoves = new Move[numOfValidMoves];

        int indexer = 0;
        for (int i = 0; i < 64; i++) {
            if (isMoves[i]) {
                validMoves[indexer] = new Move(getSquare(), i);
                indexer++;
            }
        }

        return validMoves;
    }

    // אצל הרגלי זה זהה ל-getValidMoves
    @Override
    public Move[] getMovesWithDeffence() {
        return getValidMoves();
    }

    // לרגלי: צעד קדימה חוקי רק אם המשבצת ריקה
    @Override
    public boolean isValidMove(int rowOffset, int colOffset) {
        int row = (getSquare() / 8) + rowOffset;
        int col = (getSquare() % 8) + colOffset;
        if (colOffset == 0) {
            if (Math.abs(rowOffset) == 2) {
                return isValidSquare(row, col) && !getBoard().isOccupy(row * 8 + col)
                        && !getBoard().isOccupy((row - (rowOffset / 2)) * 8 + col);
            }
            return isValidSquare(row, col) && (!getBoard().isOccupy(row * 8 + col));
        }
        return isValidAttack(rowOffset, colOffset);
    }

    // אכילה חוקית רק אם באלכסון עומד כלי של היריב
    public boolean isValidAttack(int rowOffset, int colOffset) {
        int row = (getSquare() / 8) + rowOffset;
        int col = (getSquare() % 8) + colOffset;
        return isValidSquare(row, col) && getBoard().isOccupy(row * 8 + col)
                && getBoard().getColor(row * 8 + col) != getColor();
    }

    // עותק של הכלי על לוח אחר
    @Override
    public Piece copy(Board newBoard) {
        return new Pawn(getColor(), getSquare(), newBoard, getName(), hasMoved());
    }
}