package de.sanj0.jess;

public class Piece {
    public static final byte NONE = 0;
    public static final byte PAWN = 1;
    public static final byte KNIGHT = 2;
    public static final byte BISHOP = 3;
    public static final byte ROOK = 5;
    public static final byte QUEEN = 6;
    public static final byte KING = 7;

    public static final byte LIGHT = 8;
    public static final byte DARK = 16;

    public static byte TYPE_MASK = 0b00111;
    public static byte DARK_MASK = 0b10000;
    public static byte LIGHT_MASK = 0b01000;
    public static byte COLOR_MASK = (byte) (LIGHT_MASK | DARK_MASK);

    /**
     * Casts the given int to a byte and return it
     *
     * @param i an int
     *
     * @return the int casted to a byte
     */
    public static byte b(final int i) {
        return (byte) i;
    }

    public static byte get(final byte type, final byte color) {
        return (byte) (type | color);
    }

    public static byte color(final byte piece) {
        return (byte) (piece & COLOR_MASK);
    }

    public static byte type(final byte piece) {
        return (byte) (piece & TYPE_MASK);
    }

    public static boolean isLight(final byte piece) {
        return color(piece) == LIGHT;
    }

    public static boolean isDark(final byte piece) {
        return color(piece) == DARK;
    }

    public static boolean isPawn(final byte piece) {
        return type(piece) == PAWN;
    }

    public static boolean isKnight(final byte piece) {
        return type(piece) == KNIGHT;
    }

    public static boolean isBishop(final byte piece) {
        return type(piece) == BISHOP;
    }

    public static boolean isQueen(final byte piece) {
        return type(piece) == QUEEN;
    }

    public static boolean isKing(final byte piece) {
        return type(piece) == KING;
    }

    public static byte oppositeColor(final byte myColor) {
        return myColor == DARK ? LIGHT : DARK;
    }
}
