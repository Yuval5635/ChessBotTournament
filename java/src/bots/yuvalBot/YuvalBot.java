package bots.yuvalBot;

import chess.Board;
import chess.Color;
import chess.Game;
import chess.Move;
import chess.pieces.Piece;
import utils.DebugWindow;
import utils.Utils;

public class YuvalBot {

    private Game game;
    private int[] bestScore;
    private long startTime;
    private long maxTime;

    public YuvalBot(Game game, long maxTime) {
        this.game = game;
        this.maxTime = maxTime * 1000;
    }

    public void turn() {
        this.startTime = System.currentTimeMillis();
        Move bestMove = null;
        boolean isWhite = this.game.isWhiteTurn();

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;
        bestScore = new int[] { isWhite ? Integer.MIN_VALUE : Integer.MAX_VALUE };

        int depth = 0;
        Move absBestMove = null;
        while (true) {
            depth++;
            try {
                for (Move move : this.game.getAllMoves()) {

                    this.game.turn(move);
                    int[] scores = miniMax(depth, alpha, beta);
                    this.game.undoTurn();

                    int score = Utils.sumArray(scores);
                    int bestScoreSum = Utils.sumArray(bestScore);

                    // DebugWindow.addLog("Move: " + move + " Score: " + score);

                    if (isWhite) {
                        if (score > bestScoreSum) {
                            bestScore = scores;
                            bestMove = move;
                        }
                        alpha = Math.max(alpha, bestScoreSum);
                    } else {
                        if (score < bestScoreSum) {
                            bestScore = scores;
                            bestMove = move;
                        }
                    }
                }
                absBestMove = null;
                absBestMove = bestMove.copy();
                DebugWindow.addLog("Depth: " + depth + " Best Move: " + absBestMove + " Score: "
                        + java.util.Arrays.toString(bestScore) + " Time: "
                        + ((System.currentTimeMillis() - this.startTime) / 1000.0) + "s");
            } catch (RuntimeException e) {
                this.game.undoTurn();
                break;
            }
        }
        DebugWindow.addLog("Best Move: " + absBestMove + " Score: " + java.util.Arrays.toString(bestScore) + " Depth: "
                + depth + " Time: " + ((System.currentTimeMillis() - this.startTime) / 1000.0) + "s");
        if (absBestMove != null) {
            this.game.turn(absBestMove);
        }
    }

    public int[] miniMax(int depth, int alpha, int beta) {
        int[] scores = new int[5];
        if (this.game.isFinished()) {
            // DebugWindow.addLog("Game Over! Winner: " + (this.game.isWin() == 1 ? "White"
            // : "Black"));
            scores[0] = 1000000 * this.game.playerWon().getValue(); // Return a large positive or
                                                                    // negative score based on who wins
            return scores; // Return a large positive or
                           // negative score based on who wins
        } else if (depth == 0) {
            if (System.currentTimeMillis() - this.startTime > this.maxTime) {
                throw new RuntimeException("Time limit exceeded");
            }
            return evaluateBoard();
        }

        boolean isWhite = this.game.isWhiteTurn();
        int[] bestScore = new int[5];
        bestScore[0] = isWhite ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Move[] moves = this.game.getAllMoves();
        for (Move move : moves) {
            this.game.turn(move);
            int[] minimaxScores;
            try {
                minimaxScores = miniMax(depth - 1, alpha, beta); // Recurse with reduced depth and inverted
                                                                 // alpha-beta values
            } catch (RuntimeException e) {
                this.game.undoTurn();
                throw e;
            }
            this.game.undoTurn();

            int sumScores = Utils.sumArray(minimaxScores);
            int bestScoreSum = Utils.sumArray(bestScore);

            if (isWhite) {
                if (sumScores > bestScoreSum) {
                    bestScore = minimaxScores; // Update bestScore if the current move yields a better score
                }
                alpha = Math.max(alpha, Math.max(bestScoreSum, sumScores));
                // Cutoff for White (Maximizer)
                if (beta <= alpha) {
                    break;
                }
            } else {
                if (sumScores < bestScoreSum) {
                    bestScore = minimaxScores; // Update bestScore if the current move yields a better score
                }
                beta = Math.min(beta, Math.min(bestScoreSum, sumScores));
                // Cutoff for Black (Minimizer)
                if (beta <= alpha) {
                    break;
                }
            }
        }
        return bestScore;
    }

    private int[] evaluateBoard() {
        int phase = getPhase();
        int[] mgScore = mgScore(phase);
        int[] egScore = egScore(phase);
        int[] scoreComponents = new int[5];
        for (int i = 0; i < scoreComponents.length; i++) {
            scoreComponents[i] = mgScore[i] + egScore[i];
        }

        return scoreComponents;
    }

    private int getPhase() {
        int phase = 0;
        Board board = this.game.getBoard();
        for (int i = 0; i < 64; i++) {
            if (board.isOccupy(i)) {
                phase += getPiecePhaseValue(board.getSquare(i));
            }
        }
        return phase;
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
            score += PST.getPSTValue(game.getBoard().getSquare(i), phase);
        }

        return score;
    }

    private int getNumMovesValue() {
        return this.game.getAllMoves(Color.WHITE).length - this.game.getAllMoves(Color.BLACK).length;
    }

    private int getPieceValue(int square) {
        if (!(this.game.getBoard().getSquare(square) instanceof chess.pieces.Piece))
            return 0;
        Piece piece = (Piece) this.game.getBoard().getSquare(square);
        int color = piece.getColor().getValue();
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

        return score;
    }

    private int[] mgScore(int phase) {
        int[] scores = new int[5];
        scores[0] += getAllPieceValue();
        scores[1] += getAllPSTValue(phase);
        scores[2] += getNumMovesValue() * 2;
        scores[3] += getAllattakingPiecesWithDefendingPiecese() * 7;

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