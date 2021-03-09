package de.sanj0.jess;

/**
 * A chess board!
 */
public class Board {

    private byte[] position;
    private byte colorToMove = Piece.LIGHT;

    private static final int[] CENTRE_DISTANCE = {
            3, 3, 3, 3, 3, 3, 3, 3,
            3, 2, 2, 2, 2, 2, 2, 3,
            3, 2, 1, 1, 1, 1, 2, 3,
            3, 2, 1, 0, 0, 1, 2, 3,
            3, 2, 1, 0, 0, 1, 2, 3,
            3, 2, 1, 1, 1, 1, 2, 3,
            3, 2, 2, 2, 2, 2, 2, 3,
            3, 3, 3, 3, 3, 3, 3, 3
    };

    private static final int MAX_NUM_PIECES = 32;

    public Board(final byte[] position) {
        this.position = position;

        if (position.length != 64) {
            throw new IllegalArgumentException("board data must contain 64 positions!");
        }
    }

    public static double endgame(final byte[] board) {
        int numPieces = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != Piece.NONE) {
                numPieces++;
            }
        }

        return numPieces == 0 ? 1 : 1 - (double) numPieces / MAX_NUM_PIECES;
    }

    // range: 0 - 3
    public static int distanceFromCentre(final int position) {
        return CENTRE_DISTANCE[position];
    }

    /**
     * Moves the piece from index start to index end
     *
     * @param start the index of the piece to move
     * @param end   the destination square of the move
     */
    public void move(final int start, final int end) {
        position[end] = position[start];
        position[start] = Piece.NONE;
    }

    /**
     * Gets {@link #position}.
     *
     * @return the value of {@link #position}
     */
    public byte[] getPosition() {
        return position;
    }

    /**
     * Sets {@link #position}.
     *
     * @param position the new value of {@link #position}
     */
    public void setPosition(final byte[] position) {
        this.position = position;
    }

    /**
     * Gets {@link #colorToMove}.
     *
     * @return the value of {@link #colorToMove}
     */
    public byte getColorToMove() {
        return colorToMove;
    }

    /**
     * Sets {@link #colorToMove}.
     *
     * @param colorToMove the new value of {@link #colorToMove}
     */
    public void setColorToMove(final byte colorToMove) {
        this.colorToMove = colorToMove;
    }
}
