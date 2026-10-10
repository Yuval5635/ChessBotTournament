package chess;

import java.util.ArrayList;

import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Queen;
import chess.pieces.Rook;
import utils.DebugWindow;

// הלוח: 64 משבצות ממוספרות 0 עד 63.
// 0 היא הפינה השמאלית התחתונה (צד הלבן), 63 הימנית העליונה (צד השחור).
// משבצת = שורה * 8 + עמודה.   שורה = משבצת / 8.   עמודה = משבצת % 8.
public class Board {

    // בכל תא יש כלי, או null אם המשבצת ריקה
    private Piece[] board;

    // כל המהלכים ששוחקו מתחילת המשחק, לפי הסדר
    private ArrayList<Move> moves;

    // לוח חדש בעמדת הפתיחה. לבן במשבצות 0 עד 15, שחור ב-48 עד 63
    public Board() {
        this.board = new Piece[] { new Rook(Color.WHITE, 0, this, 'R'), new Knight(Color.WHITE, 1, this, 'N'),
                new Bishop(Color.WHITE, 2, this, 'B'),
                new Queen(Color.WHITE, 3, this, 'Q'), new King(Color.WHITE, 4, this, 'K'),
                new Bishop(Color.WHITE, 5, this, 'B'),
                new Knight(Color.WHITE, 6, this, 'N'), new Rook(Color.WHITE, 7, this, 'R'),
                new Pawn(Color.WHITE, 8, this, 'P'), new Pawn(Color.WHITE, 9, this, 'P'),
                new Pawn(Color.WHITE, 10, this, 'P'),
                new Pawn(Color.WHITE, 11, this, 'P'), new Pawn(Color.WHITE, 12, this, 'P'),
                new Pawn(Color.WHITE, 13, this, 'P'),
                new Pawn(Color.WHITE, 14, this, 'P'), new Pawn(Color.WHITE, 15, this, 'P'),
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                new Pawn(Color.BLACK, 48, this, 'p'), new Pawn(Color.BLACK, 49, this, 'p'),
                new Pawn(Color.BLACK, 50, this, 'p'),
                new Pawn(Color.BLACK, 51, this, 'p'), new Pawn(Color.BLACK, 52, this, 'p'),
                new Pawn(Color.BLACK, 53, this, 'p'),
                new Pawn(Color.BLACK, 54, this, 'p'), new Pawn(Color.BLACK, 55, this, 'p'),
                new Rook(Color.BLACK, 56, this, 'r'), new Knight(Color.BLACK, 57, this, 'n'),
                new Bishop(Color.BLACK, 58, this, 'b'),
                new Queen(Color.BLACK, 59, this, 'q'), new King(Color.BLACK, 60, this, 'k'),
                new Bishop(Color.BLACK, 61, this, 'b'),
                new Knight(Color.BLACK, 62, this, 'n'), new Rook(Color.BLACK, 63, this, 'r') };
        moves = new ArrayList<Move>();
    }

    // מחזירה את כל הכלים לעמדת הפתיחה
    public void resetBoard() {
        this.board = new Piece[] { new Rook(Color.WHITE, 0, this, 'R'), new Knight(Color.WHITE, 1, this, 'N'),
                new Bishop(Color.WHITE, 2, this, 'B'),
                new Queen(Color.WHITE, 3, this, 'Q'), new King(Color.WHITE, 4, this, 'K'),
                new Bishop(Color.WHITE, 5, this, 'B'),
                new Knight(Color.WHITE, 6, this, 'N'), new Rook(Color.WHITE, 7, this, 'R'),
                new Pawn(Color.WHITE, 8, this, 'P'), new Pawn(Color.WHITE, 9, this, 'P'),
                new Pawn(Color.WHITE, 10, this, 'P'),
                new Pawn(Color.WHITE, 11, this, 'P'), new Pawn(Color.WHITE, 12, this, 'P'),
                new Pawn(Color.WHITE, 13, this, 'P'),
                new Pawn(Color.WHITE, 14, this, 'P'), new Pawn(Color.WHITE, 15, this, 'P'),
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                new Pawn(Color.BLACK, 48, this, 'p'), new Pawn(Color.BLACK, 49, this, 'p'),
                new Pawn(Color.BLACK, 50, this, 'p'),
                new Pawn(Color.BLACK, 51, this, 'p'), new Pawn(Color.BLACK, 52, this, 'p'),
                new Pawn(Color.BLACK, 53, this, 'p'),
                new Pawn(Color.BLACK, 54, this, 'p'), new Pawn(Color.BLACK, 55, this, 'p'),
                new Rook(Color.BLACK, 56, this, 'r'), new Knight(Color.BLACK, 57, this, 'n'),
                new Bishop(Color.BLACK, 58, this, 'b'),
                new Queen(Color.BLACK, 59, this, 'q'), new King(Color.BLACK, 60, this, 'k'),
                new Bishop(Color.BLACK, 61, this, 'b'),
                new Knight(Color.BLACK, 62, this, 'n'), new Rook(Color.BLACK, 63, this, 'r') };
    }

    // האם יש כלי במשבצת
    public boolean isOccupy(int square) {
        return getSquare(square) != null;
    }

    // אותו דבר, לפי עמודה (x) ושורה (y)
    public boolean isOccupy(int x, int y) {
        return isOccupy(y * 8 + x);
    }

    // הצבע של הכלי במשבצת (המשבצת צריכה להיות תפוסה)
    public Color getColor(int square) {
        return getSquare(square).getColor();
    }

    // שמה כלי במשבצת (או null כדי לרוקן אותה)
    private void setSquare(int square, Piece piece) {
        this.board[square] = piece;
    }

    // הכלי שבמשבצת, או null אם היא ריקה
    public Piece getSquare(int square) {
        return this.board[square];
    }

    // אותו דבר, לפי עמודה (x) ושורה (y)
    public Piece getSquare(int x, int y) {
        return getSquare(y * 8 + x);
    }

    // מבצעת מהלך: רושמת אותו ברשימת המהלכים, ואם הכלי יכול להגיע ליעד מזיזה אותו.
    // מחזירה true אם המהלך בוצע.
    public boolean movePiece(Move move) {
        moves.add(move);
        Piece piece = getSquare(move.fromSquare());
        if (piece == null)
            return false;
        for (Move m : piece.getValidMoves()) {
            if (m.toSquare() == move.toSquare()) {

                movePiece(piece, move.toSquare());
                return true;
            }
        }
        return false;
    }

    // מבטלת את המהלך האחרון: מחזירה את הלוח לעמדת הפתיחה, מוחקת את המהלך האחרון מהרשימה,
    // ומשחקת מחדש את כל המהלכים שנשארו.
    public void undoMove() {
        resetBoard();
        if(moves.isEmpty()){
            DebugWindow.addLog("No moves to undo.");
            return;
        }
        moves.remove(moves.size() - 1);
        for (Move move : moves) {
            movePiece(getSquare(move.fromSquare()), move.toSquare());
        }
    }

    // ההזזה עצמה: שמה את הכלי ביעד ומרוקנת את המשבצת שממנה יצא.
    // כלי שהיה ביעד נדרס, וכך אוכלים.
    private void movePiece(Piece piece, int toSquare) {
        // בודקים אם זו הצרחה לפני שהמלך זז, כי אחרי שהוא זז משבצת היעד כבר תפוסה
        boolean castling = piece instanceof King && ((King) piece).canCastle(toSquare);
        setSquare(toSquare, piece);
        setSquare(piece.getSquare(), null);
        piece.moveTo(toSquare);
        // רגלי שהגיע לשורה האחרונה הופך למלכה
        if (piece instanceof Pawn) {
            if ((toSquare / 8 == 0 && piece.getColor() == Color.BLACK)
                    || (toSquare / 8 == 7 && piece.getColor() == Color.WHITE)) {
                setSquare(toSquare,
                        new Queen(piece.getColor(), toSquare, this, piece.getColor() == Color.WHITE ? 'Q' : 'q'));
            }
        }
        // הצרחה: מזיזים גם את הצריח
        if (castling) {
            // המשבצת הראשונה בשורת הבית של המלך: 0 ללבן, 56 לשחור
            int row = piece.getColor() == Color.WHITE ? 0 : 56;
            if (toSquare == row + 2) {
                movePiece(getSquare(row), row + 3);
            } else if (toSquare == row + 6) {
                movePiece(getSquare(row + 7), row + 5);
            }
        }
    }

    // כל המהלכים האפשריים של צבע אחד.
    // סורקת את הלוח ממשבצת 0 עד 63, ואוספת את המהלכים של כל כלי בצבע הזה לפי הסדר.
    public Move[] getAllMoves(Color color) {
        Move[] allMoves = new Move[4096];
        int index = 0;

        for (int i = 0; i < 64; i++) {
            if (this.isOccupy(i) && this.getColor(i) == color) {
                Move[] pieceMoves = this.getSquare(i).getValidMoves();
                for (Move pieceMove : pieceMoves) {
                    allMoves[index] = pieceMove;
                    index++;
                }
            }
        }

        Move[] allValidMoves = new Move[index];

        for (int i = 0; i < allValidMoves.length; i++) {
            allValidMoves[i] = allMoves[i];
        }

        return allValidMoves;
    }

    public boolean isPawn(Piece piece) {
        return piece instanceof Pawn;
    }

    public boolean isPawn(int square) {
        return isOccupy(square) && isPawn(getSquare(square));
    }

    public boolean isKnight(Piece piece) {
        return piece instanceof Knight;
    }

    public boolean isKnight(int square) {
        return isOccupy(square) && isKnight(getSquare(square));
    }

    public boolean isBishop(Piece piece) {
        return piece instanceof Bishop;
    }

    public boolean isBishop(int square) {
        return isOccupy(square) && isBishop(getSquare(square));
    }

    public boolean isRook(Piece piece) {
        return piece instanceof Rook;
    }

    public boolean isRook(int square) {
        return isOccupy(square) && isRook(getSquare(square));
    }

    public boolean isQueen(Piece piece) {
        return piece instanceof Queen;
    }

    public boolean isQueen(int square) {
        return isOccupy(square) && isQueen(getSquare(square));
    }

    public boolean isKing(Piece piece) {
        return piece instanceof King;
    }

    public boolean isKing(int square) {
        return isOccupy(square) && isKing(getSquare(square));
    }

    // מדפיסה את הלוח כטקסט
    public void printBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                System.out.print("" + ((getSquare(col, row) == null) ? " " : (getSquare(col, row).getName())));
                if (col < 7)
                    System.out.print("|");
            }
            System.out.println();
            if (row < 7)
                System.out.println("-+-+-+-+-+-+-+-");
        }
    }

    // מחזירה את המערך של 64 המשבצות
    public Piece[] getBoard() {
        return board;
    }
}