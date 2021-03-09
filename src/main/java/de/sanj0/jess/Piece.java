package de.sanj0.jess;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private static Map<Byte, List<Integer>> STARTING_POSITIONS = new HashMap<Byte, List<Integer>>() {{
        put(Piece.get(PAWN, LIGHT), Arrays.asList(48, 49, 50, 51, 52, 53, 54, 55));
        put(Piece.get(KNIGHT, LIGHT), Arrays.asList(57, 62));
        put(Piece.get(BISHOP, LIGHT), Arrays.asList(58, 61));
        put(Piece.get(ROOK, LIGHT), Arrays.asList(56, 63));
        put(Piece.get(QUEEN, LIGHT), Arrays.asList(59));
        put(Piece.get(KING, LIGHT), Arrays.asList(60));
        // dark
        put(Piece.get(PAWN, DARK), Arrays.asList(8, 9, 10, 11, 12, 13, 14, 15));
        put(Piece.get(KNIGHT, DARK), Arrays.asList(1, 6));
        put(Piece.get(BISHOP, DARK), Arrays.asList(2, 5));
        put(Piece.get(ROOK, DARK), Arrays.asList(0, 7));
        put(Piece.get(QUEEN, DARK), Arrays.asList(3));
        put(Piece.get(KING, DARK), Arrays.asList(4));
    }};

    public static int rank(final int index) {
        return index / 8;
    }

    public static int file(final int index) {
        return index - rank(index) * 8;
    }

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

    public static int value(final byte piece) {
        final byte type = type(piece);
        if (type == PAWN) {
            return 1;
        } else if (type == KNIGHT || type == BISHOP) {
            return 3;
        } else if (type == ROOK) {
            return 5;
        } else if (type == QUEEN) {
            return 8;
        } else if (type == KING) {
            return Integer.MAX_VALUE;
        } else {
            throw new IllegalArgumentException(piece + " is not a valid piece.");
        }
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

    public static List<Integer> startingIndex(final byte piece) {
        return STARTING_POSITIONS.get(piece);
    }
}
