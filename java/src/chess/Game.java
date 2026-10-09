package chess;

import chess.pieces.King;
import utils.DebugWindow;
import utils.Utils;

public class Game {

    private boolean isWhiteTurn;
    private Board board;

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

    public void resetGame() {
        this.board.resetBoard();
        this.isWhiteTurn = true;
    }

    public Color playerWon() {
        boolean whiteKingAlive = false;
        boolean blackKingAlive = false;

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

    private boolean move(Move move) {
        if (this.board.movePiece(move)) {
            this.isWhiteTurn = !this.isWhiteTurn;
            return true;
        }
        return false;
    }

    public boolean turn(Move move) {
        if (isMoveValid(move)) {
            if (move(move)) {
                return isFinished();
            }
        }
        return false;
    }

    private boolean isMoveValid(Move move) {
        return this.board.isOccupy(move.fromSquare())
                && this.board.getColor(move.fromSquare()) == (this.isWhiteTurn ? Color.WHITE : Color.BLACK)
                && Utils.findIndex(this.board.getAllMoves(this.board.getColor(move.fromSquare())), move) != -1;
    }

    public boolean isMoveValid(int fromSquare, int toSquare) {
        return isMoveValid(new Move(fromSquare, toSquare));
    }

    public boolean isWhiteTurn() {
        return this.isWhiteTurn;
    }

    public int getNumOfMoves() {
        return this.board.getAllMoves(this.isWhiteTurn ? Color.WHITE : Color.BLACK).length;
    }

    public Move[] getAllMoves() {
        return getAllMoves(this.isWhiteTurn ? Color.WHITE : Color.BLACK);
    }

    public Move[] getAllMoves(Color color) {
        return this.board.getAllMoves(color);
    }

    public Board getBoard() {
        return this.board;
    }

    public void undoTurn() {
        this.board.undoMove();
        this.isWhiteTurn = !this.isWhiteTurn;
    }

    public void printBoard() {
        this.board.printBoard();
    }
}
