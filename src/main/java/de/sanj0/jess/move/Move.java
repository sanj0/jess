package de.sanj0.jess.move;

import de.sanj0.jess.Board;
import de.sanj0.jess.Piece;

import java.util.ArrayList;
import java.util.List;

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

    private List<CastleMove.CastleType> prevokedCastleRights = new ArrayList<>(2);

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
    }

    public int rating(final byte[] board) {
        final byte capturedPiece = beforeState[1];
        final int promotionRating = promotion() != Piece.NONE ? 8 : 0;
        if (capturedPiece == Piece.NONE) {
            return ratingByPosition(board) + promotionRating;
        } else {
            return Piece.value(capturedPiece) * 5 + ratingByPosition(board) + promotionRating;
        }
    }

    private int ratingByPosition(final byte[] board) {
        // if the piece is developed for the first time
        // it's a plus
        final byte me = beforeState[0];
        final int myPosition = indices[0];
        final int dstPos = indices[1];
        if (Piece.startingIndex(me).contains(myPosition)) {
            if (Piece.type(me) == Piece.QUEEN) {
                return Math.min(2, 4 - Board.distanceFromCentre(dstPos)) - 1;
            } else if (Piece.type(me) == Piece.PAWN) {
                // better to develop centre pawns
                return ratePawnAdvance(board);
            } else if (Piece.type(me) == Piece.KING) {
                // don't develop the king
                // - especially not to the centre of the board
                return -2 + Board.distanceFromCentre(dstPos);
            } else if (Piece.type(me) == Piece.ROOK){
                return Board.endgame(board) > .2 ? 0 : -1;
            } else {
                return Math.max(2, 4 - Board.distanceFromCentre(dstPos));
            }
        }
        return 0;
    }

    private int ratePawnAdvance(final byte[] board) {
        final int myPosition = indices[0];
        final boolean isCentrePawn = myPosition == 11 || myPosition == 12
                || myPosition == 51 || myPosition == 52;
        int centreModifier = isCentrePawn ? 2 : 1;
        int doubleAdvanceCentreModifier = isCentrePawn && Math.abs(indices[0] - indices[1]) == 16
                ? 2 : 0;
        int endgameModifier = Board.endgame(board) > .5 ? 0 : -1;

        return centreModifier + endgameModifier + doubleAdvanceCentreModifier;
    }

    public byte[] boardAfterMove(final byte[] position) {
        final byte[] board = new byte[position.length];
        final byte promotion = promotion();
        if (promotion != Piece.NONE) {
            for (int i = 0; i < position.length; i++) {
                if (i == indices[0]) {
                    board[i] = afterState[0];
                } else if (i == indices[1]) {
                    board[i] = promotion;
                } else {
                    board[i] = position[i];
                }
            }
        } else {
            for (int i = 0; i < position.length; i++) {
                if (i == indices[0]) {
                    board[i] = afterState[0];
                } else if (i == indices[1]) {
                    board[i] = afterState[1];
                } else {
                    board[i] = position[i];
                }
            }
        }

        return board;
    }

    // return Piece.NONE if not and the respective Queen if it is
    public byte promotion() {
        final byte myColor = Piece.color(beforeState[0]);
        if (Piece.isPawn(beforeState[0])) {
            final int rank = Piece.rank(indices[1]);
            if (myColor == Piece.LIGHT && rank == 0) {
                return Piece.get(Piece.QUEEN, Piece.LIGHT);
            } else if (myColor == Piece.DARK && rank == 7) {
                return Piece.get(Piece.QUEEN, Piece.DARK);
            }
        }

        return Piece.NONE;
    }

    /**
     * Does the move described by this
     * instance.
     *
     * @param board the board
     */
    public void doMove(final byte[] board) {
        final byte promotion = promotion();
        board[indices[0]] = afterState[0];
        if (promotion == Piece.NONE) {
            final byte me = afterState[1];
            board[indices[1]] = me;
            if (Piece.isKing(me)) {
                if (Piece.color(me) == Piece.LIGHT) {
                    prevokedCastleRights.add(CastleMove.CastleType.KING_SIDE_LIGHT);
                    prevokedCastleRights.add(CastleMove.CastleType.QUEEN_SIDE_LIGHT);
                } else {
                    prevokedCastleRights.add(CastleMove.CastleType.KING_SIDE_DARK);
                    prevokedCastleRights.add(CastleMove.CastleType.QUEEN_SIDE_DARK);
                }
            } else if (Piece.isRook(me)) {
                if (Piece.color(me) == Piece.LIGHT) {
                    if (indices[0] == 56) {
                        prevokedCastleRights.add(CastleMove.CastleType.QUEEN_SIDE_LIGHT);
                    } else if (indices[0] == 63) {
                        prevokedCastleRights.add(CastleMove.CastleType.KING_SIDE_LIGHT);
                    }
                } else {
                    if (indices[0] == 0) {
                        prevokedCastleRights.add(CastleMove.CastleType.QUEEN_SIDE_DARK);
                    } else if (indices[0] == 7) {
                        prevokedCastleRights.add(CastleMove.CastleType.KING_SIDE_DARK);
                    }
                }
            }
        } else {
            board[indices[1]] = promotion;
        }
        MoveState.allowedCastles.removeAll(prevokedCastleRights);
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
        MoveState.allowedCastles.addAll(prevokedCastleRights);
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
