package chess.pieces;

import chess.Board;
import chess.Color;
import chess.Move;

// הבסיס המשותף לכל הכלים. כל סוג כלי (רגלי, פרש...) מוסיף את חוקי התנועה שלו.
public abstract class Piece {

    // הצבע, המשבצת שבה הכלי עומד, הלוח שהוא נמצא עליו, והאות שלו (אות גדולה = לבן, קטנה = שחור)
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

    // מעדכנת את המשבצת שהכלי זוכר שהוא עומד בה
    public void moveTo(int square) {
        this.square = square;
    }

    // האם המספר הוא משבצת שקיימת על הלוח
    public boolean isValidSquare(int square) {
        return square < 64 && square >= 0;
    }

    // האם צעד של כך וכך שורות ועמודות מהמקום הנוכחי נשאר בתוך הלוח
    public boolean isValidSquare(int rowOffset, int colOffset) {
        int row = (this.square / 8) + rowOffset;
        int col = (this.square % 8) + colOffset;
        return row < 8 && row >= 0 && col < 8 && col >= 0;
    }

    // האם אפשר לזוז לשם: המשבצת בתוך הלוח, והיא ריקה או שיש בה כלי של היריב
    protected boolean isValidMove(int rowOffset, int colOffset) {
        int row = (this.square / 8) + rowOffset;
        int col = (this.square % 8) + colOffset;
        return isValidSquare(rowOffset, colOffset)
                && ((!this.board.isOccupy(row * 8 + col)) || this.board.getColor(row * 8 + col) != this.color);
    }

    // כל המהלכים שהכלי יכול לעשות עכשיו. כל סוג כלי כותב את זה בעצמו
    public abstract Move[] getValidMoves();

    // כמו getValidMoves, אבל כולל גם משבצות שעומד בהן כלי מאותו צבע
    public abstract Move[] getMovesWithDeffence();

    // עותק של הכלי על לוח אחר
    public abstract Piece copy(Board newBoard);
}
