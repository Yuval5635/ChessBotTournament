package minimaxBot;

import chess.Color;
import chess.Game;
import chess.Move;
import chess.pieces.Piece;
import utils.DebugWindow;

// הבוט. הקובץ בנוי משלושה חלקים:
// 1. turn()          - בוחר מהלך ומשחק אותו
// 2. miniMax()       - מסתכל כמה מהלכים קדימה
// 3. evaluateBoard() - נותן ציון ללוח
public class ChessBot {

    // המשחק שעליו הבוט משחק. זה אותו משחק של השחקן, לא עותק
    private Game game;
    // כמה מהלכים קדימה להסתכל (נקבע ב-Main.java)
    private int maxDepth;
    // הציון של המהלך הכי טוב שנמצא בתור הנוכחי
    private int bestScore;

    public ChessBot(Game game, int depth) {
        this.game = game;
        this.maxDepth = depth;
    }

     long start;
    public Move findBestMove() {
        start = System.currentTimeMillis();
        long limit = 2800; // 28 שניות 
        Move bestMove = null;
        // מתחילים מהמספר הכי נמוך שקיים, כדי שכל ציון אמיתי יהיה גבוה ממנו
       

        // עוברים על כל המהלכים החוקיים של מי שתורו (הבוט)
     for(int Depthcount = 1; Depthcount < 50; Depthcount++) {
            bestScore = Integer.MIN_VALUE;
            long depthStart = System.currentTimeMillis();
         for (Move move : this.game.getAllMoves()) {
        
            // משחקים את המהלך על הלוח, שואלים כמה המצב שווה, ומבטלים.
            // התשובה חוזרת מנקודת המבט של היריב, לכן המינוס שלפני miniMax.
            // שני המספרים האחרונים הם ערכי ההתחלה של alpha ו-beta (הגיזום).
            this.game.turn(move);
            int score = -miniMax(Depthcount, -10000000, 10000000);  
            this.game.undoTurn();

            // ציון גבוה מהשיא עד עכשיו: שומרים אותו ואת המהלך. בשוויון נשאר הראשון שנמצא
            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
        
            }
                

        } 
        long lastDepthTime = System.currentTimeMillis() - depthStart;
        long used = System.currentTimeMillis() - start;
        if (used + lastDepthTime * 20 > limit)
            break;

    }
    return bestMove;
    }
    
    // ===== חלק 1: בחירת מהלך =====
    // נקראת כשמגיע תור הבוט. מנסה כל מהלך אפשרי,
    // ומשחקת את זה שקיבל את הציון הכי גבוה.
    public void turn() {
        Move bestMove = null;
        bestMove = findBestMove();
        
        // מדפיסים ל-Debug Console, ומשחקים את המהלך שנבחר באמת (בלי לבטל)
        DebugWindow.addLog("Best Move: " + bestMove + " Best Score: " + bestScore+" Depth: " + maxDepth+" Time: " + ((System.currentTimeMillis() -start)/1000.0 ) + "s");
        this.game.turn(bestMove);
    }
    


    // ===== חלק 2: הסתכלות קדימה =====
    // מחזירה ציון למצב הנוכחי, מנקודת המבט של מי שתורו עכשיו,
    // בהנחה ששני הצדדים משחקים הכי טוב שלהם.
    // depth = כמה מהלכים עוד נשאר לדמיין. 
    // alpha, beta = הגבולות של הגיזום.
    public int miniMax(int depth, int alpha, int beta) {
        if (this.game.playerWon() != Color.NONE) {
            // מלך נאכל: ציון ענק. חיובי אם מי שתורו ניצח, שלילי אם הפסיד
            return 1000000 * (this.game.playerWon() == Color.WHITE ? 1 : -1)  * (this.game.isWhiteTurn() ? 1 : -1);
        } else if (depth == 0) {
            // נגמר העומק: מפסיקים לדמיין ונותנים ציון ללוח
            return evaluateBoard();
        }

        int bestScore = Integer.MIN_VALUE;
        // כל המהלכים החוקיים של מי שתורו עכשיו
        Move[] moves = this.game.getAllMoves();

        for (Move move : moves) {
            
            // משחקים מהלך, שואלים את הצד השני כמה המצב שווה לו, ומבטלים.
            // המינוס שלפני miniMax הופך את התשובה שלו לנקודת המבט שלי.
            // depth - 1 כי נשאר מהלך אחד פחות לדמיין.
            
            this.game.turn(move);
            int minimaxScore = -miniMax(depth - 1, -beta, -alpha);
            this.game.undoTurn();

            // שומרים את הציון הכי גבוה מבין המהלכים
            if (minimaxScore > bestScore) {
                bestScore = minimaxScore;
            }

            // גיזום: מעדכנים את alpha, ואם alpha הגיע ל-beta מפסיקים לבדוק את שאר המהלכים
            alpha = Math.max(alpha, minimaxScore);

            if (alpha >= beta) {
                break;
            }
        }
        // מחזירים את הציון של המהלך הכי טוב
        return bestScore;
    }

    // ===== חלק 3: ציון ללוח =====
    // הציון של מצב, מנקודת המבט של מי שתורו. כרגע: רק סכום שווי הכלים.
    private int evaluateBoard() {
        return getAllPieceValue();
    }

    // עוברת על כל 64 המשבצות ומחברת את השווי של כל הכלים
    private int getAllPieceValue() {
        int score = 0;

        for (int i = 0; i < 64; i++) {
            score += getPieceValue(i);
        }

        return score;
    }

    // השווי של הכלי במשבצת אחת.
    // כלי של מי שתורו נכנס בפלוס, כלי של הצד השני במינוס.
    private int getPieceValue(int square) {
        // משבצת ריקה שווה 0
        if (!(this.game.getBoard().getSquare(square) instanceof chess.pieces.Piece))
            return 0;
        Piece piece = (Piece) this.game.getBoard().getSquare(square);
        // 1 אם הכלי שייך למי שתורו, 1- אם לצד השני
        int color = game.isWhiteTurn() ? piece.getColor().getValue() : -piece.getColor().getValue();
        // המחיר לפי סוג הכלי

         if (piece instanceof chess.pieces.Pawn &&((square >= 18 && square <= 21) || (square >= 26 && square <= 29) || (square >= 34 && square <= 37) || (square >= 42 && square <= 45)))
            return 120 * color;

        if (piece instanceof chess.pieces.Pawn)
            return 100 * color;

        if (piece instanceof chess.pieces.Knight &&((square >= 18 && square <= 21) || (square >= 26 && square <= 29) || (square >= 34 && square <= 37) || (square >= 42 && square <= 45)))
            return 320 * color;

        if (piece instanceof chess.pieces.Knight)
            return 300 * color;
        
        if (piece instanceof chess.pieces.Bishop&&((square >= 18 && square <= 21) || (square >= 26 && square <= 29) || (square >= 34 && square <= 37) || (square >= 42 && square <= 45)))
            return 320 * color;

        if (piece instanceof chess.pieces.Bishop)
            return 300 * color;

        if (piece instanceof chess.pieces.Rook &&((square >= 18 && square <= 21) || (square >= 26 && square <= 29) || (square >= 34 && square <= 37) || (square >= 42 && square <= 45)))
            return 520 * color;

        if (piece instanceof chess.pieces.Rook)
            return 500 * color;

        if (piece instanceof chess.pieces.Queen &&((square >= 18 && square <= 21) || (square >= 26 && square <= 29) || (square >= 34 && square <= 37) || (square >= 42 && square <= 45)))
            return 1010 * color;

        if (piece instanceof chess.pieces.Queen)
            return 990 * color;

        if (piece instanceof chess.pieces.King)
            return 100000 * color;

        if ((square >= 18 && square <= 21) || (square >= 26 && square <= 29) || (square >= 34 && square <= 37) || (square >= 42 && square <= 45))
            return 20 * color;
        return 0;
    }
 }  
