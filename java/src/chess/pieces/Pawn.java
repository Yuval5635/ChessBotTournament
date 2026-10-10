package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

// רגלי: צעד אחד קדימה, שני צעדים מהשורה ההתחלתית, ואוכל באלכסון
public class Pawn extends Piece {

    public Pawn(Color color, int square, Board board, char name) {
        super(color, square, board, name);
    }

    // כל המהלכים האפשריים של הכלי. מסמנים true בכל משבצת שאפשר להגיע אליה,
    // ובסוף הופכים את הסימונים למערך של מהלכים.
    @Override
    public Move[] getValidMoves() {
        boolean[] isMoves = new boolean[64];

        // הכיוון קדימה: לבן עולה בשורות (1), שחור יורד (1-)
        int direction = this.color.getValue();

        // צעד אחד קדימה אם המשבצת ריקה. מהשורה ההתחלתית אפשר גם שני צעדים
        if (isValidMove(direction, 0)) {
            isMoves[this.square + (direction * 8)] = true;
            if ((this.square / 8 == 6 && this.color == Color.BLACK)
                    || (this.square / 8 == 1 && this.color == Color.WHITE)) {
                if (isValidMove(direction * 2, 0)) {
                    isMoves[this.square + (direction * 16)] = true;
                }
            }
        }

        // אכילה באלכסון, לשני הצדדים
        if (isValidAttack(direction, -1)) {
            isMoves[this.square + (direction * 8) - 1] = true;
        }

        if (isValidAttack(direction, 1)) {
            isMoves[this.square + (direction * 8) + 1] = true;
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
                validMoves[indexer] = new Move(this.square, i);
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
        int row = (this.square / 8) + rowOffset;
        int col = (this.square % 8) + colOffset;
        return row < 8 && row >= 0 && (!this.board.isOccupy(row * 8 + col));
    }

    // אכילה חוקית רק אם באלכסון עומד כלי של היריב
    public boolean isValidAttack(int rowOffset, int colOffset) {
        int row = (this.square / 8) + rowOffset;
        int col = (this.square % 8) + colOffset;
        return row < 8 && row >= 0 && col < 8 && col >= 0 && this.board.isOccupy(row * 8 + col)
                && this.board.getColor(row * 8 + col) != this.color;
    }

    // עותק של הכלי על לוח אחר
    @Override
    public Piece copy(Board newBoard) {
        return new Pawn(this.color, this.square, newBoard, this.name);
    }
}