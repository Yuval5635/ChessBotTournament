package chess;

// שני הצבעים. לכל צבע יש גם מספר: לבן = 1, שחור = 1-
public enum Color {
    WHITE(1),
    BLACK(-1),
    NONE(0);

    private final int value;

    Color(int value) {
        this.value = value;
    }

    // המספר של הצבע (1 או 1-)
    public int getValue() {
        return value;
    }
}
