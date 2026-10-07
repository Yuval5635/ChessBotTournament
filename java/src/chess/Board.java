package chess;

import java.util.ArrayList;

import chess.pieces.Bishop;
import chess.pieces.King;
import chess.pieces.Knight;
import chess.pieces.Pawn;
import chess.pieces.Piece;
import chess.pieces.Queen;
import chess.pieces.Rook;
import utils.DebugWindow;

public class Board {

    private Piece[] board;

    private ArrayList<Move> moves;

    public Board() {
        this.board = new Piece[] { new Rook(Color.WHITE, 0, this, 'R'), new Knight(Color.WHITE, 1, this, 'N'),
                new Bishop(Color.WHITE, 2, this, 'B'),
                new Queen(Color.WHITE, 3, this, 'Q'), new King(Color.WHITE, 4, this, 'K'),
                new Bishop(Color.WHITE, 5, this, 'B'),
                new Knight(Color.WHITE, 6, this, 'N'), new Rook(Color.WHITE, 7, this, 'R'),
                new Pawn(Color.WHITE, 8, this, 'P'), new Pawn(Color.WHITE, 9, this, 'P'),
                new Pawn(Color.WHITE, 10, this, 'P'),
                new Pawn(Color.WHITE, 11, this, 'P'), new Pawn(Color.WHITE, 12, this, 'P'),
                new Pawn(Color.WHITE, 13, this, 'P'),
                new Pawn(Color.WHITE, 14, this, 'P'), new Pawn(Color.WHITE, 15, this, 'P'),
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                new Pawn(Color.BLACK, 48, this, 'p'), new Pawn(Color.BLACK, 49, this, 'p'),
                new Pawn(Color.BLACK, 50, this, 'p'),
                new Pawn(Color.BLACK, 51, this, 'p'), new Pawn(Color.BLACK, 52, this, 'p'),
                new Pawn(Color.BLACK, 53, this, 'p'),
                new Pawn(Color.BLACK, 54, this, 'p'), new Pawn(Color.BLACK, 55, this, 'p'),
                new Rook(Color.BLACK, 56, this, 'r'), new Knight(Color.BLACK, 57, this, 'n'),
                new Bishop(Color.BLACK, 58, this, 'b'),
                new Queen(Color.BLACK, 59, this, 'q'), new King(Color.BLACK, 60, this, 'k'),
                new Bishop(Color.BLACK, 61, this, 'b'),
                new Knight(Color.BLACK, 62, this, 'n'), new Rook(Color.BLACK, 63, this, 'r') };
        moves = new ArrayList<Move>();
    }

    public void resetBoard() {
        this.board = new Piece[] { new Rook(Color.WHITE, 0, this, 'R'), new Knight(Color.WHITE, 1, this, 'N'),
                new Bishop(Color.WHITE, 2, this, 'B'),
                new Queen(Color.WHITE, 3, this, 'Q'), new King(Color.WHITE, 4, this, 'K'),
                new Bishop(Color.WHITE, 5, this, 'B'),
                new Knight(Color.WHITE, 6, this, 'N'), new Rook(Color.WHITE, 7, this, 'R'),
                new Pawn(Color.WHITE, 8, this, 'P'), new Pawn(Color.WHITE, 9, this, 'P'),
                new Pawn(Color.WHITE, 10, this, 'P'),
                new Pawn(Color.WHITE, 11, this, 'P'), new Pawn(Color.WHITE, 12, this, 'P'),
                new Pawn(Color.WHITE, 13, this, 'P'),
                new Pawn(Color.WHITE, 14, this, 'P'), new Pawn(Color.WHITE, 15, this, 'P'),
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                null, null, null, null, null, null, null, null,
                new Pawn(Color.BLACK, 48, this, 'p'), new Pawn(Color.BLACK, 49, this, 'p'),
                new Pawn(Color.BLACK, 50, this, 'p'),
                new Pawn(Color.BLACK, 51, this, 'p'), new Pawn(Color.BLACK, 52, this, 'p'),
                new Pawn(Color.BLACK, 53, this, 'p'),
                new Pawn(Color.BLACK, 54, this, 'p'), new Pawn(Color.BLACK, 55, this, 'p'),
                new Rook(Color.BLACK, 56, this, 'r'), new Knight(Color.BLACK, 57, this, 'n'),
                new Bishop(Color.BLACK, 58, this, 'b'),
                new Queen(Color.BLACK, 59, this, 'q'), new King(Color.BLACK, 60, this, 'k'),
                new Bishop(Color.BLACK, 61, this, 'b'),
                new Knight(Color.BLACK, 62, this, 'n'), new Rook(Color.BLACK, 63, this, 'r') };
    }

    public boolean isOccupy(int square) {
        return getSquare(square) != null;
    }

    public boolean isOccupy(int x, int y) {
        return isOccupy(y * 8 + x);
    }

    public Color getColor(int square) {
        return getSquare(square).getColor();
    }

    private void setSquare(int square, Piece piece) {
        this.board[square] = piece;
    }

    public Piece getSquare(int square) {
        return this.board[square];
    }

    public Piece getSquare(int x, int y) {
        return getSquare(y * 8 + x);
    }

    public boolean movePiece(Move move) {
        moves.add(move);
        Piece piece = getSquare(move.fromSquare());
        if (piece == null)
            return false;
        for (Move m : piece.getValidMoves()) {
            if (m.toSquare() == move.toSquare()) {

                movePiece(piece, move.toSquare());
                return true;
            }
        }
        return false;
    }

    public void undoMove() {
        resetBoard();
        if(moves.isEmpty()){
            DebugWindow.addLog("No moves to undo.");
            return;
        }
        moves.remove(moves.size() - 1);
        for (Move move : moves) {
            movePiece(getSquare(move.fromSquare()), move.toSquare());
        }
    }

    private void movePiece(Piece piece, int toSquare) {
        setSquare(toSquare, piece);
        setSquare(piece.getSquare(), null);
        piece.moveTo(toSquare);
        if (piece instanceof Pawn) {
            if ((toSquare / 8 == 0 && piece.getColor() == Color.BLACK)
                    || (toSquare / 8 == 7 && piece.getColor() == Color.WHITE)) {
                setSquare(toSquare,
                        new Queen(piece.getColor(), toSquare, this, piece.getColor() == Color.WHITE ? 'Q' : 'q'));
            }
        }
        if (piece instanceof King) {
            if (((King) piece).canCastle(toSquare)) {
                if (toSquare == 2 + (piece.getColor() == Color.WHITE ? 0 : 56)) {
                    movePiece(getSquare(0 + (piece.getColor() == Color.WHITE ? 0 : 56)),
                            3 + (piece.getColor() == Color.WHITE ? 0 : 56));
                    setSquare(3 + (piece.getColor() == Color.WHITE ? 0 : 56),
                            getSquare(0 + (piece.getColor() == Color.WHITE ? 0 : 56)));
                    setSquare(getSquare(0 + (piece.getColor() == Color.WHITE ? 0 : 56)).getSquare(), null);
                } else if (toSquare == 6 + (piece.getColor() == Color.WHITE ? 0 : 56)) {
                    movePiece(getSquare(7 + (piece.getColor() == Color.WHITE ? 0 : 56)),
                            5 + (piece.getColor() == Color.WHITE ? 0 : 56));
                    setSquare(5 + (piece.getColor() == Color.WHITE ? 0 : 56),
                            getSquare(7 + (piece.getColor() == Color.WHITE ? 0 : 56)));
                    setSquare(getSquare(7 + (piece.getColor() == Color.WHITE ? 0 : 56)).getSquare(), null);
                }
            }
        }
    }

    public Move[] getAllMoves(Color color) {
        Move[] allMoves = new Move[4096];
        int index = 0;

        for (int i = 0; i < 64; i++) {
            if (this.isOccupy(i) && this.getColor(i) == color) {
                Move[] pieceMoves = this.getSquare(i).getValidMoves();
                for (Move pieceMove : pieceMoves) {
                    allMoves[index] = pieceMove;
                    index++;
                }
            }
        }

        Move[] allValidMoves = new Move[index];

        for (int i = 0; i < allValidMoves.length; i++) {
            allValidMoves[i] = allMoves[i];
        }

        return allValidMoves;
    }

    public void printBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                System.out.print("" + ((getSquare(col, row) == null) ? " " : (getSquare(col, row).getName())));
                if (col < 7)
                    System.out.print("|");
            }
            System.out.println();
            if (row < 7)
                System.out.println("-+-+-+-+-+-+-+-");
        }
    }

    public Piece[] getBoard() {
        return board;
    }
}