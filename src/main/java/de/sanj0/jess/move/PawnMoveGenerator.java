package de.sanj0.jess.move;

import de.sanj0.jess.Piece;

import java.util.ArrayList;
import java.util.List;

public class PawnMoveGenerator extends MoveGenerator {

    @Override
    public List<Integer> pseudoLegalMoves(final byte[] board, final int position) {
        final List<Integer> moves = new ArrayList<>(3);
        final byte color = Piece.color(board[position]);
        boolean isSingleAdvancePossible = false;
        if (color == Piece.DARK) {
            //FIXME: array index out of bounds possible
            isSingleAdvancePossible = board[position + 8] == Piece.NONE;
            // normal +1 extend if the square is empty and in bounds
            addIfEmptyAndInBounds(moves, board, position + 8);
            // capture left if not on a rank, opposite color piece and in bounds
            if (position % 8 != 0) {
                addIfColorAndInBounds(moves, board, position + 7, Piece.LIGHT);
            }
            // capture right if not on a rank, opposite color piece and in bounds
            if ((position + 1) % 8 != 0) {
                addIfColorAndInBounds(moves, board, position + 9, Piece.LIGHT);
            }
            // long advance from starting position
            if (position >= 8 && position <= 15 && isSingleAdvancePossible) {
                addIfEmptyAndInBounds(moves, board, position + 16);
            }
        } else if (color == Piece.LIGHT) {
            //FIXME: array index out of bounds possible
            isSingleAdvancePossible = board[position - 8] == Piece.NONE;
            // normal +1 extend if the square is empty and in bounds
            addIfEmptyAndInBounds(moves, board, position - 8);
            // capture left if not on a rank, opposite color piece and in bounds
            if (position % 8 != 0) {
                addIfColorAndInBounds(moves, board, position - 9, Piece.DARK);
            }
            // capture right if not on a rank, opposite color piece and in bounds
            if ((position + 1) % 8 != 0) {
                addIfColorAndInBounds(moves, board, position - 7, Piece.DARK);
            }
            // long advance from starting position
            if (position >= 48 && position <= 55 && isSingleAdvancePossible) {
                addIfEmptyAndInBounds(moves, board, position - 16);
            }
        }

        return moves;
    }

    private static void addIfEmptyAndInBounds(final List<Integer> moves, final byte[] board, final int dst) {
        if (dst <= 63 && dst >= 0 && board[dst] == Piece.NONE) {
            moves.add(dst);
        }
    }

    private static void addIfColorAndInBounds(final List<Integer> moves, final byte[] board, final int dst, final byte color) {
        if (dst <= 63 && dst >= 0 && Piece.color(board[dst]) == color) {
            moves.add(dst);
        }
    }

    @Override
    protected int[][] moveSchemesUpperHalf() {
        return new int[0][];
    }

    @Override
    protected void removeFriendlyFire(final List<Integer> moves, final byte[] board, final int myIndex) {
    }
}
