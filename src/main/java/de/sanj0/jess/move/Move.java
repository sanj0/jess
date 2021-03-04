package de.sanj0.jess.move;

/**
 * A move has to indices it affects, the start square and the end square. Both
 * have a before and an after state.
 * TODO(sanj0): castling
 */
public class Move {

    /**
     * The two indices affected by the move
     */
    private final int[] indices;
    /**
     * Contains the two pieces on the two affected squares before the move
     */
    private final byte[] beforeState;
    /**
     * Contains the two pieces on the two affected squares after the move
     */
    private final byte[] afterState;

    /**
     * constructor.
     *
     * @param indices the two indices affected by this move
     * @param beforeState the before state of the two indices
     * @param afterState the after state of the two indices
     * @throws IllegalArgumentException when any of the arrays does not have exactly 2
     * elements
     */
    public Move(final int[] indices, final byte[] beforeState, final byte[] afterState) {
        this.indices = indices;
        this.beforeState = beforeState;
        this.afterState = afterState;

        if (indices.length != 2 || beforeState.length != 2 || afterState.length != 2) {
            throw new IllegalArgumentException("all arrays passed to Move.<init> must be of length 2");
        }
    }

    /**
     * Does the move described by this
     * instance.
     *
     * @param board the board
     */
    public void doMove(final byte[] board) {
        board[indices[0]] = afterState[0];
        board[indices[1]] = afterState[1];
    }

    /**
     * Undoes the move described by this
     * instance.
     *
     * @param board the board
     */
    public void undoMove(final byte[] board) {
        board[indices[0]] = beforeState[0];
        board[indices[1]] = beforeState[1];
    }

    /**
     * Gets {@link #indices}.
     *
     * @return the value of {@link #indices}
     */
    public int[] getIndices() {
        return indices;
    }

    /**
     * Gets {@link #beforeState}.
     *
     * @return the value of {@link #beforeState}
     */
    public byte[] getBeforeState() {
        return beforeState;
    }

    /**
     * Gets {@link #afterState}.
     *
     * @return the value of {@link #afterState}
     */
    public byte[] getAfterState() {
        return afterState;
    }
}
