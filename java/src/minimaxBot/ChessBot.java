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
        boolean isWhite = this.game.isWhiteTurn();
        bestScore = isWhite ? Integer.MIN_VALUE : Integer.MAX_VALUE;

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (Move move : this.game.getAllMoves()) {

            this.game.turn(move);
            int score = miniMax(this.maxDepth - 1, alpha, beta);
            this.game.undoTurn();

            if (isWhite) {
                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
                alpha = Math.max(alpha, bestScore);
            } else {
                if (score < bestScore) {
                    bestScore = score;
                    bestMove = move;
                }
                beta = Math.min(beta, bestScore);
            }
        }

        DebugWindow.addLog("Best Move: " + bestMove + " Best Score: " + bestScore);
        if (bestMove != null) {
            this.game.turn(bestMove);
        }
    }

    public int miniMax(int depth, int alpha, int beta) {
        if (this.game.isFinished()) {
            return 1000000 * this.game.playerWon().getValue();
        } else if (depth == 0) {
            return evaluateBoard();
        }

        boolean isWhite = this.game.isWhiteTurn();
        int bestScore = isWhite ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Move[] moves = this.game.getAllMoves();

        for (Move move : moves) {
            this.game.turn(move);
            int minimaxScore = miniMax(depth - 1, alpha, beta);
            this.game.undoTurn();

            if (isWhite) {
                bestScore = Math.max(bestScore, minimaxScore);
                alpha = Math.max(alpha, bestScore);
                // Cutoff for White (Maximizer)
                if (beta <= alpha) {
                    break;
                }
            } else {
                bestScore = Math.min(bestScore, minimaxScore);
                beta = Math.min(beta, bestScore);
                // Cutoff for Black (Minimizer)
                if (beta <= alpha) {
                    break;
                }
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
        int color = piece.getColor().getValue();
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