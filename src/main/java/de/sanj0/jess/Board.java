package de.sanj0.jess;

/**
 * A chess board!
 */
public class Board {

    private byte[] position;
    private byte colorToMove = Piece.LIGHT;

    public Board(final byte[] position) {
        this.position = position;

        if (position.length != 64) {
            throw new IllegalArgumentException("board data must contain 64 positions!");
        }
    }

    /**
     * Moves the piece from index start to index end
     *
     * @param start the index of the piece to move
     * @param end   the destination square of the move
     */
    public void move(final int start, final int end) {
        // check for legality

        // move the piece
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
