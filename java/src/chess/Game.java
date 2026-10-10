package chess;

import chess.pieces.King;
import utils.DebugWindow;
import utils.Utils;

// מנהל המשחק: מחזיק את הלוח, זוכר תור מי, מבצע ומבטל מהלכים.
// אלה הכלים שהבוט משתמש בהם.
public class Game {

    // true כשתור הלבן, false כשתור השחור
    private boolean isWhiteTurn;
    private Board board;

    // משחק חדש: הלבן מתחיל, לוח בעמדת הפתיחה.
    // בנוסף נרשמות שתי פקודות שאפשר להקליד ב-Debug Console: undo 2 וגם reset.
    public Game() {
        this.isWhiteTurn = true;
        this.board = new Board();
        DebugWindow.addLog("Game started");
        DebugWindow.addInputListener(input -> {
            if (input.equals("undo 2")) {
                undoTurn();
                undoTurn();
                DebugWindow.addLog("Undid last two moves to get the player's turn back");
            }
        });
        DebugWindow.addInputListener(input -> {
            if(input.equals("reset")){
                resetGame();
                DebugWindow.addLog("Game reset");
            }
        });
    }

    // מחזירה את הלוח לעמדת הפתיחה ואת התור ללבן
    public void resetGame() {
        this.board.resetBoard();
        this.isWhiteTurn = true;
    }

    // מי ניצח: 1 = הלבן, 1- = השחור, 0 = עוד אף אחד.
    // ניצחון כאן הוא כשהמלך של הצד השני כבר לא על הלוח (נאכל). אין בדיקה של שח או מט.
    public Color playerWon() {
        boolean whiteKingAlive = false;
        boolean blackKingAlive = false;

        // עוברים על כל הלוח ובודקים אילו מלכים עדיין עליו
        for (int i = 0; i < 64; i++) {
            if (this.board.isOccupy(i) && this.board.getSquare(i) instanceof King) {
                if (this.board.getColor(i) == Color.WHITE)
                    whiteKingAlive = true;
                else if (this.board.getColor(i) == Color.BLACK)
                    blackKingAlive = true;
            }
        }

        if (!whiteKingAlive)
            return Color.BLACK;
        else if (!blackKingAlive)
            return Color.WHITE;
        else
            return Color.NONE;
    }

    public boolean isFinished(){
        return playerWon() != Color.NONE;
    }

    // מזיזה כלי על הלוח, ואם זה הצליח מעבירה את התור לצד השני
    private boolean move(Move move) {
        if (this.board.movePiece(move)) {
            this.isWhiteTurn = !this.isWhiteTurn;
            return true;
        }
        return false;
    }

    // מבצעת מהלך אם הוא חוקי, והתור עובר לצד השני.
    // מחזירה true רק אם המהלך הזה ניצח את המשחק.
    public boolean turn(Move move) {
        if (isMoveValid(move)) {
            if (move(move)) {
                return isFinished();
            }
        }
        return false;
    }

    // מהלך חוקי אם: יש כלי במשבצת המוצא, הוא בצבע של מי שתורו,
    // והמהלך נמצא ברשימת המהלכים האפשריים של הצבע הזה.
    private boolean isMoveValid(Move move) {
        return this.board.isOccupy(move.fromSquare())
                && this.board.getColor(move.fromSquare()) == (this.isWhiteTurn ? Color.WHITE : Color.BLACK)
                && Utils.findIndex(this.board.getAllMoves(this.board.getColor(move.fromSquare())), move) != -1;
    }

    // אותה בדיקה, כשמקבלים שני מספרים במקום Move
    public boolean isMoveValid(int fromSquare, int toSquare) {
        return isMoveValid(new Move(fromSquare, toSquare));
    }

    // האם עכשיו תור הלבן
    public boolean isWhiteTurn() {
        return this.isWhiteTurn;
    }

    // כמה מהלכים אפשריים יש למי שתורו עכשיו
    public int getNumOfMoves() {
        return this.board.getAllMoves(this.isWhiteTurn ? Color.WHITE : Color.BLACK).length;
    }

    // כל המהלכים האפשריים של מי שתורו עכשיו
    public Move[] getAllMoves() {
        return this.board.getAllMoves(this.isWhiteTurn ? Color.WHITE : Color.BLACK);
    }

    // מחזירה את הלוח
    public Board getBoard() {
        return this.board;
    }

    // מבטלת את המהלך האחרון ומחזירה את התור לצד הקודם
    public void undoTurn() {
        this.board.undoMove();
        this.isWhiteTurn = !this.isWhiteTurn;
    }

    // מדפיסה את הלוח כטקסט
    public void printBoard() {
        this.board.printBoard();
    }
}
