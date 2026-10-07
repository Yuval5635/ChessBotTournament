package minimaxBot;

import chess.Color;
import chess.pieces.Piece;

public class PST {

    public static final int[] PAWN = {
            0, 0, 0, 0, 0, 0, 0, 0,
            5, 10, 10, -20, -20, 10, 10, 5,
            5, -5, -10, 0, 0, -10, -5, 5,
            0, 0, 0, 20, 20, 0, 0, 0,
            5, 5, 10, 25, 25, 10, 5, 5,
            10, 10, 20, 30, 30, 20, 10, 10,
            50, 50, 50, 50, 50, 50, 50, 50,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    private static final int[] KNIGHT = {
            -50, -40, -30, -30, -30, -30, -40, -50,
            -40, -20, 0, 0, 0, 0, -20, -40,
            -30, 0, 10, 15, 15, 10, 0, -30,
            -30, 5, 15, 20, 20, 15, 5, -30,
            -30, 0, 15, 20, 20, 15, 0, -30,
            -30, 5, 10, 15, 15, 10, 5, -30,
            -40, -20, 0, 5, 5, 0, -20, -40,
            -50, -40, -30, -30, -30, -30, -40, -50
    };

    public static final int[] BISHOP = {
            -20, -10, -10, -10, -10, -10, -10, -20,
            -10, 5, 0, 0, 0, 0, 5, -10,
            -10, 10, 5, 10, 10, 5, 10, -10,
            -10, 5, 5, 10, 10, 5, 5, -10,
            -10, 0, 10, 10, 10, 10, 0, -10,
            -10, 0, 10, 10, 10, 10, 0, -10,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -20, -10, -10, -10, -10, -10, -10, -20
    };

    private static final int[] ROOK = {
            0, 0, 0, 5, 5, 0, 0, 0,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            -5, 0, 0, 0, 0, 0, 0, -5,
            5, 10, 10, 10, 10, 10, 10, 5,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    private static final int[] QUEEN = {
            -20, -10, -10, -5, -5, -10, -10, -20,
            -10, 0, 0, 0, 0, 0, 0, -10,
            -10, 0, 5, 5, 5, 5, 0, -10,
            -5, 0, 5, 5, 5, 5, 0, -5,
            0, 0, 5, 5, 5, 5, 0, -5,
            -10, 5, 5, 5, 5, 5, 0, -10,
            -10, 0, 5, 0, 0, 0, 0, -10,
            -20, -10, -10, -5, -5, -10, -10, -20
    };

    public static final int[] KING_MG = {
            20, 30, 10, 0, 0, 10, 30, 20,
            20, 20, 0, 0, 0, 0, 20, 20,
            -10, -20, -20, -20, -20, -20, -20, -10,
            -20, -30, -30, -40, -40, -30, -30, -20,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30,
            -30, -40, -40, -50, -50, -40, -40, -30
    };

    private static final int[] KING_EG = {
            -50, -40, -30, -20, -20, -30, -40, -50,
            -30, -20, -10, 0, 0, -10, -20, -30,
            -30, -10, 20, 30, 30, 20, -10, -30,
            -30, -10, 30, 40, 40, 30, -10, -30,
            -30, -10, 30, 40, 40, 30, -10, -30,
            -30, -10, 20, 30, 30, 20, -10, -30,
            -30, -30, 0, 0, 0, 0, -30, -30,
            -50, -30, -30, -30, -30, -30, -30, -50
    };

    private static final int[] OPENING = {
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            10, 20, 20, 20, 20, 20, 20, 10,
            20, 20, 20, 20, 20, 20, 20, 20,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0,
            0, 0, 0, 0, 0, 0, 0, 0
    };

    private static int flipSquare(int square) {
        return square ^ 56;
    }

    public static int getPSTValue(Piece piece, int phase) {
        if (piece == null) {
            return 0;
        }
        if (phase == 24) {
            return (int) (1.5
                    * OPENING[piece.getColor() == Color.WHITE ? piece.getSquare() : flipSquare(piece.getSquare())]);
        }
        if (piece instanceof chess.pieces.Pawn) {
            return (int) (1.5
                    * PAWN[piece.getColor() == Color.WHITE ? piece.getSquare() : flipSquare(piece.getSquare())]);
        }
        if (piece instanceof chess.pieces.Knight) {
            return (int) (1.5
                    * KNIGHT[piece.getColor() == Color.WHITE ? piece.getSquare() : flipSquare(piece.getSquare())]);
        }
        if (piece instanceof chess.pieces.Bishop) {
            return (int) (1.5
                    * BISHOP[piece.getColor() == Color.WHITE ? piece.getSquare() : flipSquare(piece.getSquare())]);
        }
        if (piece instanceof chess.pieces.Rook) {
            return (int) (1.5
                    * ROOK[piece.getColor() == Color.WHITE ? piece.getSquare() : flipSquare(piece.getSquare())]);
        }
        if (piece instanceof chess.pieces.Queen) {
            return (int) (1.5
                    * QUEEN[piece.getColor() == Color.WHITE ? piece.getSquare() : flipSquare(piece.getSquare())]);
        }
        if (piece instanceof chess.pieces.King) {
            return phase > 12 ? (int) (1.5
                    * KING_MG[piece.getColor() == Color.WHITE ? piece.getSquare() : flipSquare(piece.getSquare())])
                    : (int) (1.5 * KING_EG[piece.getColor() == Color.WHITE ? piece.getSquare()
                            : flipSquare(piece.getSquare())]);
        }
        return 0;
    }
}