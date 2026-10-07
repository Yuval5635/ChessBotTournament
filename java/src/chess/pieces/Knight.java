package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

public class Knight extends Piece {

    public Knight(Color color, int square, Board board, char name) {
        super(color, square, board, name);
    }

    @Override
    public Move[] getValidMoves() {
        boolean[] isMoves = new boolean[64];

        for (int i = -2; i <= 2; i++) {
            if (i == 0)
                continue;
            for (int j = -1; j < 2; j += 2) {
                if (isValidMove(i, (3 - Math.abs(i)) * j)) {
                    isMoves[this.square + (i * 8) + ((3 - Math.abs(i)) * j)] = true;
                }
            }
        }

        int numOfValidMoves = 0;
        for (boolean isMove : isMoves) {
            if (isMove)
                numOfValidMoves++;
        }

        Move[] validMoves = new Move[numOfValidMoves];

        int indexer = 0;
        for (int i = 0; i < 64; i++) {
            if (isMoves[i]) {
                validMoves[indexer] = new Move(this.square, i);
                indexer++;
            }
        }

        return validMoves;
    }

    @Override
    public Move[] getMovesWithDeffence() {
        boolean[] isMoves = new boolean[64];

        for (int i = -2; i <= 2; i++) {
            if (i == 0)
                continue;
            for (int j = -1; j < 2; j += 2) {
                if (isValidSquare(i, (3 - Math.abs(i)) * j)) {
                    isMoves[this.square + (i * 8) + ((3 - Math.abs(i)) * j)] = true;
                }
            }
        }

        int numOfMoves = 0;
        for (boolean isMove : isMoves) {
            if (isMove)
                numOfMoves++;
        }

        Move[] moves = new Move[numOfMoves];

        int indexer = 0;
        for (int i = 0; i < 64; i++) {
            if (isMoves[i]) {
                moves[indexer] = new Move(this.square, i);
                indexer++;
            }
        }

        return moves;
    }

    @Override
    public Piece copy(Board newBoard) {
        return new Knight(this.color, this.square, newBoard, this.name);
    }
}