package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

// מלך: צעד אחד לכל כיוון, ובנוסף הצרחה
public class King extends Piece {

    // האם המלך כבר זז במשחק (חשוב להצרחה)
    boolean isMoved;

    public King(Color color, int square, Board board, char name) {
        super(color, square, board, name);
        this.isMoved = false;
    }

    // כל המהלכים האפשריים של הכלי. מסמנים true בכל משבצת שאפשר להגיע אליה,
    // ובסוף הופכים את הסימונים למערך של מהלכים.
    @Override
    public Move[] getValidMoves() {
        boolean[] isMoves = new boolean[64];

        // המשבצות שמסביב למלך
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isValidMove(i, j)) {
                    isMoves[this.square + (i * 8) + j] = true;
                }
            }
        }

        // הצרחה: בדיקה לכל אחת מארבע משבצות היעד
        if (canCastle(2)) {
            isMoves[2] = true;
        }
        if (canCastle(6)) {
            isMoves[6] = true;
        }
        if (canCastle(58)) {
            isMoves[58] = true;
        }
        if (canCastle(62)) {
            isMoves[62] = true;
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

    // כמו getValidMoves, אבל כולל גם משבצות שעומד בהן כלי מאותו צבע
    @Override
    public Move[] getMovesWithDeffence() {
        boolean[] isMoves = new boolean[64];

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (isValidSquare(i, j)) {
                    isMoves[this.square + (i * 8) + j] = true;
                }
            }
        }

        if (canCastle(2)) {
            isMoves[2] = true;
        }
        if (canCastle(6)) {
            isMoves[6] = true;
        }
        if (canCastle(58)) {
            isMoves[58] = true;
        }
        if (canCastle(62)) {
            isMoves[62] = true;
        }

        int numOfMoves = 0;
        for (boolean isMove : isMoves) {
            if (isMove)
                numOfMoves++;
        }

        Move[] moves = new Move[numOfMoves];

        int indexer = 0;
        for (int i = 0; i < 64; i++) {
            if (isMoves[i]) {
                moves[indexer] = new Move(this.square, i);
                indexer++;
            }
        }

        return moves;
    }

    // בודקת אם מותר להצריח למשבצת toSquare: המלך והצריח עוד לא זזו, והמשבצות שביניהם ריקות.
    // כל מלך מצריח רק בשורה שלו: הלבן למשבצות 2 ו-6, השחור ל-58 ו-62.
    public boolean canCastle(int toSquare) {
        if (this.isMoved)
            return false;
        // המשבצת הראשונה בשורת הבית של המלך: 0 ללבן, 56 לשחור
        int row = this.color == Color.WHITE ? 0 : 56;
        if (toSquare == row + 2) {
            if (this.board.getSquare(row) instanceof Rook && !((Rook) this.board.getSquare(row)).isMoved()) {
                if (!this.board.isOccupy(row + 1) && !this.board.isOccupy(row + 2) && !this.board.isOccupy(row + 3)) {
                    return true;
                }
            }
        } else if (toSquare == row + 6) {
            if (this.board.getSquare(row + 7) instanceof Rook && !((Rook) this.board.getSquare(row + 7)).isMoved()) {
                if (!this.board.isOccupy(row + 5) && !this.board.isOccupy(row + 6)) {
                    return true;
                }
            }
        }
        return false;
    }

    // כשהמלך זז: אם זו הצרחה מזיזים גם את הצריח, ואז זוכרים שהמלך כבר זז
    @Override
    public void moveTo(int square) {
        if (canCastle(square)) {
            if (this.color == Color.WHITE) {
                if (square == 2) {
                    this.board.movePiece(new Move(0, 3));
                } else if (square == 6) {
                    this.board.movePiece(new Move(7, 5));
                }
            } else {
                if (square == 58) {
                    this.board.movePiece(new Move(56, 59));
                } else if (square == 62) {
                    this.board.movePiece(new Move(63, 60));
                }
            }
        }
        super.moveTo(square);
        this.isMoved = true;
    }

    // עותק של הכלי על לוח אחר
    @Override
    public Piece copy(Board newBoard) {
        return new King(this.color, this.square, newBoard, this.name);
    }
}