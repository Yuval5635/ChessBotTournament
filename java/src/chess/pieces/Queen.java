package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

public class Queen extends Piece {

    public Queen(Color color, int square, Board board, char name) {
        super(color, square, board, name);
    }

    @Override
    public Move[] getValidMoves() {
        boolean[] isMoves = new boolean[64];

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                for (int k = 1; true; k++) {
                    if (isValidMove(i * k, j * k)) {
                        isMoves[this.square + ((i * 8) + j) * k] = true;
                        if (this.board.isOccupy(this.square + ((i * 8) + j) * k)) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }

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

        for (int i = -1; i <= 1; i += 2) {
            for (int j = -1; j <= 1; j += 2) {
                for (int k = 1; true; k++) {
                    if (isValidSquare(i * k, j * k)) {
                        isMoves[this.square + ((i * 8) + j) * k] = true;
                        if (this.board.isOccupy(this.square + ((i * 8) + j) * k)) {
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
        }

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

    @Override
    public Piece copy(Board newBoard) {
        return new Queen(this.color, this.square, newBoard, this.name);
    }
}