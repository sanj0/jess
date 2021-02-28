package de.sanj0.jess;

import java.util.ArrayList;
import java.util.List;

/**
 * Moves
 */
public class Moves {

    public static Move move(final byte[] position, final int startIndex, final int endIndex) {
        final int[] indices = {startIndex, endIndex};
        final byte[] before = {position[startIndex], position[endIndex]};
        final byte[] after = {Piece.NONE, position[startIndex]};
        return new Move(indices, before, after);
    }

    public static List<Integer> pseudoLegalMoves(final byte[] board, final int position) {
        final byte piece = board[position];
        final List<Integer> pseudoLegal = new ArrayList<>(8);

        if (Piece.isKnight(piece)) {
            addPseudoLegalKnightMoves(position, pseudoLegal);
        }

        return pseudoLegal;
    }

    private static void addPseudoLegalKnightMoves(final int position, final List<Integer> pseudoLegal) {
        final int freeSquaresUp = freeSquaresUp(position);
        final int freeSquaresDown = 7 - freeSquaresUp;
        final int freeSquaresRight = freeSquaresRight(position);
        final int freeSquaresLeft = 7 - freeSquaresRight;
        if (freeSquaresUp >= 2) {
            // -17 and -15 may be possible
            if (freeSquaresRight >= 2) {
                pseudoLegal.add(position - 15);
                pseudoLegal.add(position - 6);
            } else if (freeSquaresRight >= 1) {
                pseudoLegal.add(position - 6);
            }

            if (freeSquaresLeft >= 2) {
                pseudoLegal.add(position - 10);
                pseudoLegal.add(position - 17);
            } else if (freeSquaresLeft >= 1) {
                pseudoLegal.add(-10);
            }
        } else if (freeSquaresUp >= 1) {

        }

        if (freeSquaresDown >= 2) {

        }
    }

    public static int freeSquaresUp(final int position) {
        return position / 8;
    }

    public static int freeSquaresDown(final int position) {
        return 7 - freeSquaresUp(position);
    }

    public static int freeSquaresRight(final int position) {
        return (position % 8) - 1;
    }

    public static int freeSquaresLeft(final int position) {
        return 7 - freeSquaresRight(position);
    }
}
