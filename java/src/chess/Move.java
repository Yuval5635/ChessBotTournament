package chess;

public record Move(int fromSquare, int toSquare) {
    public Move(int fromSquare, int toSquare) {
        this.fromSquare = fromSquare;
        this.toSquare = toSquare;
    }

    @Override
    public String toString() {
        return "From Square: " + this.fromSquare + " To Square: " + this.toSquare;
    }
}
