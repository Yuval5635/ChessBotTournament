package minimaxBot;

import chess.Game;
import chess.Move;
import chess.pieces.Piece;
import utils.DebugWindow;

public class ChessBot {

    private Game game;
    private int maxDepth;
    private int bestScore;

    public ChessBot(Game game, int depth) {
        this.game = game;
        this.maxDepth = depth;
    }

    public void turn() {
        Move bestMove = null;
        bestScore = Integer.MIN_VALUE;

        for (Move move : this.game.getAllMoves()) {

            this.game.turn(move);
            int score = miniMax(this.maxDepth - 1, Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.game.undoTurn();

            if (score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }
        DebugWindow.addLog("Best Move: " + bestMove + " Best Score: " + bestScore);
        this.game.turn(bestMove);
    }

    public int miniMax(int depth, int alpha, int beta) {
        if (this.game.isWin() != 0) {
            return 1000000 * this.game.isWin() * (this.game.isWhiteTurn() ? 1 : -1);
        } else if (depth == 0) {
            return evaluateBoard();
        }

        int bestScore = Integer.MIN_VALUE;
        Move[] moves = this.game.getAllMoves();

        for (Move move : moves) {
            
            this.game.turn(move);
            int minimaxScore = miniMax(depth - 1, -beta, -alpha);
            this.game.undoTurn();

            if (minimaxScore > bestScore) {
                bestScore = minimaxScore;
            }

            alpha = Math.max(alpha, minimaxScore);

            if (alpha >= beta) {
                break;
            }
        }
        return bestScore;
    }

    private int evaluateBoard() {
        return getAllPieceValue();
    }

    private int getAllPieceValue() {
        int score = 0;

        for (int i = 0; i < 64; i++) {
            score += getPieceValue(i);
        }

        return score;
    }

    private int getPieceValue(int square) {
        if (!(this.game.getBoard().getSquare(square) instanceof chess.pieces.Piece))
            return 0;
        Piece piece = (Piece) this.game.getBoard().getSquare(square);
        int color = game.isWhiteTurn() ? piece.getColor().getValue() : -piece.getColor().getValue();
        if (piece instanceof chess.pieces.Pawn)
            return 100 * color;
        if (piece instanceof chess.pieces.Knight)
            return 300 * color;
        if (piece instanceof chess.pieces.Bishop)
            return 300 * color;
        if (piece instanceof chess.pieces.Rook)
            return 500 * color;
        if (piece instanceof chess.pieces.Queen)
            return 990 * color;
        if (piece instanceof chess.pieces.King)
            return 100000 * color;
        return 0;
    }
}