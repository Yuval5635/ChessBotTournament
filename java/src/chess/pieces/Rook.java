package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

// צריח: זז ישר, בשורה או בעמודה, עד שנתקעים בכלי או בקצה הלוח
public class Rook extends Piece {

    public Rook(Color color, int square, Board board, char name) {
        super(color, square, board, name);
    }
    public Rook(Color color, int square, Board board, char name, boolean hasMoved) {
        super(color, square, board, name, hasMoved);
    }

    // כל המהלכים האפשריים של הכלי. מסמנים true בכל משבצת שאפשר להגיע אליה,
    // ובסוף הופכים את הסימונים למערך של מהלכים.
    @Override
    public Move[] getValidMoves() {
        boolean[] isMoves = new boolean[64];

        // i הוא הכיוון (1- או 1). הלולאה הראשונה זזה בשורות, השנייה בעמודות
        for (int i = -1; i < 2; i += 2) {
            for (int j = i; true; j += i) {
                if (isValidMove(j, 0)) {
                    isMoves[getSquare() + (j * 8)] = true;
                    if (getBoard().isOccupy(j * 8 + getSquare())) {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int j = i; true; j += i) {
                if (isValidMove(0, j)) {
                    isMoves[getSquare() + j] = true;
                    if (getBoard().isOccupy(j + getSquare())) {
                        break;
                    }
                } else {
                    break;
                }
            }
        }

        // סופרים כמה משבצות סומנו, ובונים מהן את מערך המהלכים
        int numOfValidMoves = 0;
        for (boolean isMove : isMoves) {
            if (isMove)
                numOfValidMoves++;
        }

        Move[] validMoves = new Move[numOfValidMoves];

        int indexer = 0;
        for (int i = 0; i < 64; i++) {
            if (isMoves[i]) {
                validMoves[indexer] = new Move(getSquare(), i);
                indexer++;
            }
        }

        return validMoves;
    }

    // כמו getValidMoves, אבל כולל גם משבצות שעומד בהן כלי מאותו צבע
    @Override
    public Move[] getMovesWithDeffence() {
        boolean[] isMoves = new boolean[64];

        for (int i = -1; i < 2; i += 2) {
            for (int j = i; true; j += i) {
                if (isValidSquare(j, 0)) {
                    isMoves[getSquare() + (j * 8)] = true;
                    if (getBoard().isOccupy(j * 8 + getSquare())) {
                        break;
                    }
                } else {
                    break;
                }
            }
            for (int j = i; true; j += i) {
                if (isValidSquare(0, j)) {
                    isMoves[getSquare() + j] = true;
                    if (getBoard().isOccupy(j + getSquare())) {
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
                moves[indexer] = new Move(getSquare(), i);
                indexer++;
            }
        }

        return moves;
    }

    // עותק של הכלי על לוח אחר
    @Override
    public Piece copy(Board newBoard) {
        return new Rook(getColor(), getSquare(), newBoard, getName(), hasMoved());
    }
}