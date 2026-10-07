package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

public class Rook extends Piece {

    boolean isMoved;

    public Rook(Color color, int square, Board board, char name) {
        super(color, square, board, name);
        this.isMoved = false;
    }

    @Override
    public Move[] getValidMoves() {
        boolean[] isMoves = new boolean[64];

        for (int i = -1; i < 2; i += 2) {
            for (int j = i; true; j += i) {
                if (isValidMove(j, 0)) {
                    isMoves[this.square + (j * 8)] = true;
                    if (this.board.isOccupy(j * 8 + this.square)) {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int j = i; true; j += i) {
                if (isValidMove(0, j)) {
                    isMoves[this.square + j] = true;
                    if (this.board.isOccupy(j + this.square)) {
                        break;
                    }
                } else {
                    break;
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

        for (int i = -1; i < 2; i += 2) {
            for (int j = i; true; j += i) {
                if (isValidSquare(j, 0)) {
                    isMoves[this.square + (j * 8)] = true;
                    if (this.board.isOccupy(j * 8 + this.square)) {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int j = i; true; j += i) {
                if (isValidSquare(0, j)) {
                    isMoves[this.square + j] = true;
                    if (this.board.isOccupy(j + this.square)) {
                        break;
                    }
                } else {
                    break;
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

    public boolean isMoved() {
        return isMoved;
    }

    @Override
    public void moveTo(int square) {
        super.moveTo(square);
        this.isMoved = true;
    }

    @Override
    public Piece copy(Board newBoard) {
        return new Rook(this.color, this.square, newBoard, this.name);
    }
}