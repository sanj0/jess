package de.sanj0.jess.move;

import de.sanj0.jess.Piece;

import java.util.ArrayList;
import java.util.List;

public class RookMoveGenerator extends MoveGenerator {

    // should be pretty easy to generate on the fly
    @Override
    public List<Integer> pseudoLegalMoves(final byte[] board, final int origin) {
        final List<Integer> moves = new ArrayList<>();
        final int rank = origin / 8;
        // the index at which the rank starts
        final int rankMin = rank * 8;
        // the index at which the rank end
        final int rankMax = rankMin + 7;
        final int file = origin - rank * 8;
        final int fileMin = file;
        final int fileMax = 56 + file;

        addLine(moves, rankMin, rankMax, origin, board, -1, 1);
        addLine(moves, fileMin, fileMax, origin, board, -8, 8);
        return moves;
    }

    private void addLine(final List<Integer> moves, final int min, final int max, final int origin, final byte[] board, final int offset1, final int offset2) {
        final byte myColor = Piece.color(board[origin]);
        // always iterate 7 times
        int squareIndex = origin + offset1;
        // start with all squares on the left
        int offsetForNextSquare = offset1;
        for (int i = 0; i < 8; i++) {
            if (squareIndex < 0 || squareIndex > 63){
                if (offsetForNextSquare == offset2) break;
                else {
                    offsetForNextSquare = offset2;
                    squareIndex = origin + offset2;
                }
            }
            if (squareIndex < min) {
                offsetForNextSquare = offset2;
                squareIndex = origin + offset2;
                continue;
            } else if (squareIndex > max) {
                // all horizontal moves are done
                break;
            }
            final byte piece = board[squareIndex];
            if (piece != Piece.NONE) {
                if (Piece.color(piece) != myColor) {
                    moves.add(squareIndex);
                }
                if (offsetForNextSquare == offset1) {
                    offsetForNextSquare = offset2;
                    squareIndex = origin + offset2;
                    continue;
                } else {
                    break;
                }
            }
            moves.add(squareIndex);
            squareIndex += offsetForNextSquare;
        }
    }

    @Override
    protected int[][] moveSchemesUpperHalf() {
        return new int[0][];
    }

    @Override
    protected void removeFriendlyFire(final List<Integer> moves, final byte[] board, final int myIndex) {
        // nothing to do - generator method already accounts for that
    }
}
