package de.sanj0.jess;

/**
 * starting position:
 * rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR some other stuff
 */
public class BoardPositions {

    public static final String STARTING_POSITION = "rnbqkbnr/pppppppp/8/8/8/8/PPPPPPPP/RNBQKBNR";

    public static final char PAWN = 'p';
    public static final char KNIGHT = 'n';
    public static final char BISHOP = 'b';
    public static final char ROOK = 'r';
    public static final char QUEEN = 'q';
    public static final char KING = 'k';

    /**
     * Pareses the given FEN position
     * into a board position stored in
     * an array length 64
     *
     * @param FEN a FEN position
     * @return the board position as a byte array length 64 as
     * specified by the given FEN string
     */
    public static byte[] parseFEN(final String FEN) {
        final String positionString = FEN.split(" ")[0];
        final byte[] board = new byte[64];
        int pointer = 0;

        for (final char c : positionString.toCharArray()) {
            if (pointer >= 64) {
                throw new InvalidFENException(positionString);
            }
            if (Character.isLetter(c)) {
                // piece
                board[pointer] = pieceFromFEN(c);
                pointer++;
            } else if (Character.isDigit(c)) {
                // pointer advance
                pointer += Character.getNumericValue(c);
            }
        }

        if (pointer != 64) {
            throw new InvalidFENException(positionString);
        }

        return board;
    }

    public static byte pieceFromFEN(final char piece) {
        final byte color = Character.isLowerCase(piece) ? Piece.DARK : Piece.LIGHT;
        final char p = Character.toLowerCase(piece);
        byte type = Piece.NONE;
        switch(p) {
            case PAWN:
                type = Piece.PAWN;
                break;
            case KNIGHT:
                type = Piece.KNIGHT;
                break;
            case BISHOP:
                type = Piece.BISHOP;
                break;
            case ROOK:
                type = Piece.ROOK;
                break;
            case QUEEN:
                type = Piece.QUEEN;
                break;
            case KING:
                type = Piece.KING;
                break;
        }

        // assignment by compound operator to
        // avoid the need to cast from int
        return type |= color;
    }
}
