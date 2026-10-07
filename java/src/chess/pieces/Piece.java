package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

public abstract class Piece {

    protected Color color;
    protected int square;
    protected Board board;
    protected char name;

    protected Piece(Color color, int square, Board board, char name) {
        this.color = color;
        this.square = square;
        this.board = board;
        this.name = name;
    }

    public int getSquare() {
        return this.square;
    }

    public Color getColor() {
        return this.color;
    }

    public char getName() {
        return this.name;
    }

    public void moveTo(int square) {
        this.square = square;
    }

    public boolean isValidSquare(int square) {
        return square < 64 && square >= 0;
    }

    public boolean isValidSquare(int rowOffset, int colOffset) {
        int row = (this.square / 8) + rowOffset;
        int col = (this.square % 8) + colOffset;
        return row < 8 && row >= 0 && col < 8 && col >= 0;
    }

    protected boolean isValidMove(int rowOffset, int colOffset) {
        int row = (this.square / 8) + rowOffset;
        int col = (this.square % 8) + colOffset;
        return isValidSquare(rowOffset, colOffset)
                && ((!this.board.isOccupy(row * 8 + col)) || this.board.getColor(row * 8 + col) != this.color);
    }

    public abstract Move[] getValidMoves();

    public abstract Move[] getMovesWithDeffence();

    public abstract Piece copy(Board newBoard);
}
