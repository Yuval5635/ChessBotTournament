package chess;

import minimaxBot.yuvalBot.YuvalBot;
import utils.DebugWindow;

public class Main {
    private Game game;
    YuvalBot chessBot;

    public Main() {
        this.game = new Game();
        this.chessBot = new YuvalBot(game, 4);
        DebugWindow.addLog("Setup complete");
    }

    public Board getBoard() {
        return game.getBoard();
    }

    public void update() {
        if(isWin()) {
            DebugWindow.addLog("Game Over!");
            return;
        }
        if (isWhiteTurn()) {
            String playerMove = DebugWindow.getInput();
            if (isMoveValid(playerMove)) {
                String[] parts = playerMove.split(" ");
                if (parts.length == 2) {
                    try {
                        int fromSquare = Integer.parseInt(parts[0]);
                        int toSquare = Integer.parseInt(parts[1]);
                        DebugWindow.addLog("Player moved: " + fromSquare + " to " + toSquare);
                        if (turn(fromSquare, toSquare)) {
                            DebugWindow.addLog("Player wins!");
                        }
                    } catch (NumberFormatException e) {
                        DebugWindow.addLog("Invalid input format. Please enter two integers separated by a space.");
                    }
                } else {
                    DebugWindow.addLog("Invalid input format. Please enter two integers separated by a space.");
                }
            } else if (playerMove != null && !playerMove.trim().isEmpty()) {
                DebugWindow.addLog("Invalid input format. Please enter two integers separated by a space.");
            }
        } else {
            botTurn();
        }
    
    }

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

    public boolean isMoveValid(int fromSquare, int toSquare) {
        return game.isMoveValid(fromSquare, toSquare);
    }

    public boolean turn(int fromSquare, int toSquare) {
        return game.turn(new Move(fromSquare, toSquare));
    }

    public void botTurn() {
        chessBot.turn();
    }

    public boolean isWhiteTurn() {
        return game.isWhiteTurn();
    }

    public boolean isWin() {
        return game.isFinished();
    }
}