package chess;

import bots.kfirBot.ChessBot;
import bots.yuvalBot.YuvalBot;
import utils.DebugWindow;

// נקודת הכניסה מצד Java. החלון של פייתון (Main.py) יוצר אחד כזה
// וקורא ל-update() שוב ושוב. הקובץ רק מחבר בין השחקן, המשחק והבוט.
public class Main {
    private Game game;
    YuvalBot yuvalChessBot;
    ChessBot chessBot;
    private boolean isStart = false;

    // רץ פעם אחת בהתחלה: יוצר משחק, ויוצר בוט שמקבל את אותו משחק.
    // המספר הוא העומק: כמה מהלכים קדימה הבוט מסתכל.
    public Main() {
        this.game = new Game();
        this.yuvalChessBot = new YuvalBot(game, 25);
        this.chessBot = new ChessBot(game, 5);
        DebugWindow.addLog("Setup complete");


        DebugWindow.addInputListener(input -> {
            if (input == null)
                return;
            if (input.equals("start")) {
                this.isStart = true;
                DebugWindow.addLog("Game started");
            }
        });
        DebugWindow.addInputListener(input -> {
            if (input.equals("undo 2")) {
                this.game.undoTurn();
                this.game.undoTurn();
                DebugWindow.addLog("Undid last two moves to get the player's turn back");
            }
        });
        DebugWindow.addInputListener(input -> {
            if(input.equals("reset")){
                this.game.resetGame();
                DebugWindow.addLog("Game reset");
            }
        });
    }

    // מחזירה את הלוח, כדי שפייתון יוכל לצייר אותו
    public Board getBoard() {
        return game.getBoard();
    }

    // נקראת מפייתון 60 פעם בשנייה.
    // אם תור הלבן והמשחק לא נגמר: בודקת אם השחקן הקליד מהלך ומבצעת אותו.
    // אם תור השחור והמשחק לא נגמר: תור הבוט.
    // אחרי שמישהו ניצח לא קורה כלום.
    public void update() {
        if (this.isStart) {
            if (isWin()) {
                DebugWindow.addLog("Game Over!");
                return;
            }
            if (isWhiteTurn()) {
                yuvalChessBot.turn();
                DebugWindow.addLog("White Played");
            } else if (!isWin()) {
                // תור הבוט
                chessBot.turn();
                DebugWindow.addLog("Black Played");
            }
        }

    }

    // בודקת שהטקסט הוא שני מספרים עם רווח ביניהם, ושהמהלך חוקי
    public boolean isMoveValid(String moveStr) {
        if (moveStr == null || moveStr.trim().isEmpty()) {
            return false;
        }
        String[] parts = moveStr.split(" ");
        if (parts.length != 2) {
            return false;
        }
        try {
            int fromSquare = Integer.parseInt(parts[0]);
            int toSquare = Integer.parseInt(parts[1]);
            return isMoveValid(fromSquare, toSquare);
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // בודקת אם מהלך חוקי (שואלת את המשחק)
    public boolean isMoveValid(int fromSquare, int toSquare) {
        return game.isMoveValid(fromSquare, toSquare);
    }

    // מבצעת מהלך. מחזירה true אם המהלך ניצח את המשחק
    public boolean turn(int fromSquare, int toSquare) {
        return game.turn(new Move(fromSquare, toSquare));
    }

    // האם עכשיו תור הלבן
    public boolean isWhiteTurn() {
        return game.isWhiteTurn();
    }

    // האם מישהו כבר ניצח
    public boolean isWin() {
        return game.isFinished();
    }
}