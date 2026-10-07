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

    public int isWin() {
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
            return -1;
        else if (!blackKingAlive)
            return 1;
        else
            return 0;
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
                return isWin() != 0;
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
        return this.board.getAllMoves(this.isWhiteTurn ? Color.WHITE : Color.BLACK);
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
