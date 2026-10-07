package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

public class Pawn extends Piece {

    public Pawn(Color color, int square, Board board, char name) {
        super(color, square, board, name);
    }

    @Override
    public Move[] getValidMoves() {
        boolean[] isMoves = new boolean[64];

        int direction = this.color.getValue();

        if (isValidMove(direction, 0)) {
            isMoves[this.square + (direction * 8)] = true;
            if ((this.square / 8 == 6 && this.color == Color.BLACK)
                    || (this.square / 8 == 1 && this.color == Color.WHITE)) {
                if (isValidMove(direction * 2, 0)) {
                    isMoves[this.square + (direction * 16)] = true;
                }
            }
        }

        if (isValidAttack(direction, -1)) {
            isMoves[this.square + (direction * 8) - 1] = true;
        }

        if (isValidAttack(direction, 1)) {
            isMoves[this.square + (direction * 8) + 1] = true;
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
        return getValidMoves();
    }

    @Override
    public boolean isValidMove(int rowOffset, int colOffset) {
        int row = (this.square / 8) + rowOffset;
        int col = (this.square % 8) + colOffset;
        return row < 8 && row >= 0 && (!this.board.isOccupy(row * 8 + col));
    }

    public boolean isValidAttack(int rowOffset, int colOffset) {
        int row = (this.square / 8) + rowOffset;
        int col = (this.square % 8) + colOffset;
        return row < 8 && row >= 0 && col < 8 && col >= 0 && this.board.isOccupy(row * 8 + col)
                && this.board.getColor(row * 8 + col) != this.color;
    }

    @Override
    public Piece copy(Board newBoard) {
        return new Pawn(this.color, this.square, newBoard, this.name);
    }
}