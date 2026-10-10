package chess;

// מהלך אחד: מאיזו משבצת (fromSquare) ולאיזו משבצת (toSquare). המשבצות ממוספרות 0 עד 63
public record Move(int fromSquare, int toSquare) {
    public Move(int fromSquare, int toSquare) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
    }

    // איך המהלך נראה כשמדפיסים אותו (למשל ב-Debug Console)
    @Override
    public String toString() {
        return "From Square: " + this.fromSquare + " To Square: " + this.toSquare;
    }
}
