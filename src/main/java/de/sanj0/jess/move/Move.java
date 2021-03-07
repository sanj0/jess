package de.sanj0.jess.move;

import de.sanj0.jess.Board;
import de.sanj0.jess.Piece;

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

    public int rating(final byte[] board) {
        final byte capturedPiece = beforeState[1];
        if (capturedPiece == Piece.NONE) {
            return ratingByPosition(board);
        } else {
            return Piece.value(capturedPiece) * 5 + ratingByPosition(board);
        }
    }

    private int ratingByPosition(final byte[] board) {
        // if the piece is developed for the first time
        // it's a plus
        final byte me = beforeState[0];
        final int myPosition = indices[0];
        if (Piece.startingIndex(me).contains(myPosition)) {
            if (Piece.type(me) == Piece.QUEEN) {
                return 1 + (3 - Board.distanceFromCentre(myPosition));
            } else if (Piece.type(me) == Piece.PAWN) {
                // better to develop centre pawns
                return myPosition == 11 || myPosition == 12
                        || myPosition == 51 || myPosition == 52 ? 3 : 2;
            } else if (Piece.type(me) == Piece.KING) {
                // don't develop the king
                // - especially not to the centre of the board
                return -4 + Board.distanceFromCentre(myPosition);
            } else if (Piece.type(me) == Piece.ROOK){
                return 2;
            } else {
                return 3 + (3 - Board.distanceFromCentre(myPosition));
            }
        }
        return 0;
    }

    public byte[] boardAfterMove(final byte[] position) {
        final byte[] board = new byte[position.length];
        for (int i = 0; i < position.length; i++) {
            if (i == indices[0]) {
                board[i] = afterState[0];
            } else if (i == indices[1]) {
                board[i] = afterState[1];
            } else {
                board[i] = position[i];
            }
        }

        return board;
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

    //TODO(sanj0) actualy notation
    public String notation() {
        return indices[0] + "-" + indices[1];
    }
}
