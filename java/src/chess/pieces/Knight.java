package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

// פרש: קופץ שתי משבצות בכיוון אחד ואחת בכיוון השני
public class Knight extends Piece {

    public Knight(Color color, int square, Board board, char name) {
        super(color, square, board, name);
    }

    public Knight(Color color, int square, Board board, char name, boolean hasMoved) {
        super(color, square, board, name, hasMoved);
    }

    // כל המהלכים האפשריים של הכלי. מסמנים true בכל משבצת שאפשר להגיע אליה,
    // ובסוף הופכים את הסימונים למערך של מהלכים.
    @Override
    public Move[] getValidMoves() {
        boolean[] isMoves = new boolean[64];

        // i = כמה שורות לזוז (2-, 1-, 1, 2). מספר העמודות משלים ל-3, לשני הצדדים
        for (int i = -2; i <= 2; i++) {
            if (i == 0)
                continue;
            for (int j = -1; j < 2; j += 2) {
                if (isValidMove(i, (3 - Math.abs(i)) * j)) {
                    isMoves[getSquare() + (i * 8) + ((3 - Math.abs(i)) * j)] = true;
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

        for (int i = -2; i <= 2; i++) {
            if (i == 0)
                continue;
            for (int j = -1; j < 2; j += 2) {
                if (isValidSquare(i, (3 - Math.abs(i)) * j)) {
                    isMoves[getSquare() + (i * 8) + ((3 - Math.abs(i)) * j)] = true;
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
        return new Knight(getColor(), getSquare(), newBoard, getName(), hasMoved());
    }
}