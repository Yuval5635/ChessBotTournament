package minimaxBot;

import chess.Board;
import chess.Game;
import chess.Move;
import chess.pieces.Piece;
import utils.DebugWindow;
import utils.Utils;

public class ChessBot {

    private Game game;
    private int maxDepth;
    private int[] bestScore;

    public ChessBot(Game game, int depth) {
        this.game = game;
        this.maxDepth = depth;
    }

    public void turn() {
        Move bestMove = null;
        bestScore = new int[]{Integer.MAX_VALUE};
        int phase = 0;

        // DebugWindow.addLog(this.game.getAllMoves().length + " Moves:  ");

        for (int i = 0; i < 64; i++) {
            if (this.game.getBoard().isOccupy(i)) {
                phase += getPiecePhaseValue(this.game.getBoard().getSquare(i));
            }
        }

        for (Move move : this.game.getAllMoves()) {

            this.game.turn(move);
            int[] scores = miniMax(this.maxDepth + (phase > 10 ? 0 : (((40 - getNumMovesValue()) / 25) * 2)),
                    Integer.MIN_VALUE, Integer.MAX_VALUE);
            this.game.undoTurn();

            int score = Utils.sumArray(scores);
            int bestScoreSum = Utils.sumArray(bestScore);

            // DebugWindow.addLog("Move:  " + move + "  Score:  " + score);

            if (score < bestScoreSum) {
                bestScore = scores;
                bestMove = move;
            }
        }
        DebugWindow.addLog("Best Move: " + bestMove + " Best Score: [" + bestScore[0] + ", " + bestScore[1] + ", " + bestScore[2] + ", " + bestScore[3] + ", " + bestScore[4] + "]");
        this.game.turn(bestMove);
    }

    public int[] miniMax(int depth, int alpha, int beta) {
        int[] scores = new int[5];
        if (this.game.isWin() != 0) {
            // DebugWindow.addLog("Game Over! Winner: " + (this.game.isWin() == 1 ? "White"
            // : "Black"));
            scores[0] = 1000000 * this.game.isWin() * (this.game.isWhiteTurn() ? 1 : -1); // Return a large positive or
                                                                                     // negative score based on who wins
            return scores; // Return a large positive or
                                                                                     // negative score based on who wins
        } else if (depth == 0) {
            return evaluateBoard();
        }

        int[] bestScore = new int[5];
        bestScore[0] = Integer.MIN_VALUE;
        Move[] moves = this.game.getAllMoves();

        for (Move move : moves) {
            // if (depth == 1 && game.getBoard().isOccupy(move.toSquare())) depth++;
            this.game.turn(move);
            int[] minimaxScores = miniMax(depth - 1, -beta, -alpha); // Recurse with reduced depth and inverted alpha-beta values
            for (int i = 0; i < minimaxScores.length; i++) {
                minimaxScores[i] = -minimaxScores[i]; // Invert the score for the opponent's perspective
            }
            this.game.undoTurn();
            
            int sumScores = Utils.sumArray(minimaxScores);
            int bestScoreSum = Utils.sumArray(bestScore);

            if (sumScores > bestScoreSum) {
                bestScore = minimaxScores; // Update bestScore if the current move yields a better score
            }
            if (sumScores >= 1000000 || sumScores <= -1000000) {
                // DebugWindow.addLog("bestScore length: " + bestScore.length + " minimaxScores length: " + minimaxScores.length);
                bestScore[4] += Math.signum(sumScores) * 50;
            }
            alpha = Math.max(alpha, sumScores);

            if (alpha >= beta) {
                break; // Beta cut-off
            }
        }
        // if (Math.abs(bestScore) > 9000){
        // bestScore -= bestScore * Math.signum(bestScore);
        // }
        return bestScore;
    }

    private int[] evaluateBoard() {
        int phase = 0;
        Board board = this.game.getBoard();
        for (int i = 0; i < 64; i++) {
            if (board.isOccupy(i)) {
                phase += getPiecePhaseValue(board.getSquare(i));
            }
        }

        int[] mgScore = mgScore(phase);
        int[] egScore = egScore(phase);
        int[] scoreComponents = new int[5];
        for (int i = 0; i < scoreComponents.length; i++) {
            scoreComponents[i] = mgScore[i] + egScore[i];
        }

        return scoreComponents;
    }

    private int getAllPieceValue() {
        int score = 0;

        for (int i = 0; i < 64; i++) {
            score += getPieceValue(i);
        }

        return score;
    }

    private int getAllPSTValue(int phase) {
        int score = 0;

        for (int i = 0; i < 64; i++) {
            score += PST.getPSTValue(game.getBoard().getSquare(i), phase) * (this.game.isWhiteTurn() ? -1 : 1);
        }

        return score;
    }

    private int getNumMovesValue() {
        return this.game.getAllMoves().length * (this.game.isWhiteTurn() ? -1 : 1);
    }

    private int getPieceValue(int square) {
        if (!(this.game.getBoard().getSquare(square) instanceof chess.pieces.Piece))
            return 0;
        Piece piece = (Piece) this.game.getBoard().getSquare(square);
        int color = game.isWhiteTurn() ? piece.getColor().getValue() : -piece.getColor().getValue();
        if (piece instanceof chess.pieces.Pawn)
            return 110 * color;
        if (piece instanceof chess.pieces.Knight)
            return 352 * color;
        if (piece instanceof chess.pieces.Bishop)
            return 363 * color;
        if (piece instanceof chess.pieces.Rook)
            return 550 * color;
        if (piece instanceof chess.pieces.Queen)
            return 990 * color;
        if (piece instanceof chess.pieces.King)
            return 100000 * color; // Arbitrary high value for the king
        return 0;
    }

    private int getPiecePhaseValue(Object piece) {
        if (piece instanceof chess.pieces.Knight)
            return 1;
        if (piece instanceof chess.pieces.Bishop)
            return 1;
        if (piece instanceof chess.pieces.Rook)
            return 2;
        if (piece instanceof chess.pieces.Queen)
            return 4;
        return 0;
    }

    private int getAllattakingPiecesWithDefendingPiecese() {
        int score = 0;

        for (int i = 0; i < 64; i++) {
            if (this.game.getBoard().isOccupy(i)) {
                Piece piece = this.game.getBoard().getSquare(i);
                Move[] moves = piece.getMovesWithDeffence();
                for (Move move : moves) {
                    if (this.game.getBoard().isOccupy(move.toSquare())) {
                        Piece targetPiece = this.game.getBoard().getSquare(move.toSquare());
                        if (targetPiece.getColor() == piece.getColor()) {
                            score += piece.getColor().getValue();
                        }
                    }
                }
            }
        }

        return score * (this.game.isWhiteTurn() ? 1 : -1);
    }

    private int[] mgScore(int phase) {
        int[] scores = new int[5];
        scores[0] += getAllPieceValue();
        scores[1] += getAllPSTValue(phase);
        scores[2] += getNumMovesValue() * 2;
        scores[3] += getAllattakingPiecesWithDefendingPiecese() * 10;

        for (int i = 0; i < scores.length; i++) {
            scores[i] = (scores[i] * phase) / 24;
        }

        return scores;
    }

    private int[] egScore(int phase) {
        int[] scores = new int[5];
        scores[0] += getAllPieceValue();
        scores[1] += getAllPSTValue(phase);
        scores[2] += getNumMovesValue() * 2;
        scores[3] += getAllattakingPiecesWithDefendingPiecese() * 10;

        for (int i = 0; i < scores.length; i++) {
            scores[i] = (scores[i] * (24 - phase)) / 24;
        }

        return scores;
    }
}