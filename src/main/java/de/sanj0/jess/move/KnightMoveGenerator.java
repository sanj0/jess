package de.sanj0.jess.move;

import de.sanj0.jess.Piece;

import java.util.List;

/**
 * Generates knight moves
 */
public class KnightMoveGenerator extends MoveGenerator {
    private static final int[][] KNIGHT_MOVES_UPPER_HALF = {
            {0, 10, 17}, // -> {63, 53, 46}
            {1, 11, 16, 18},
            {2, 8, 12, 17, 19},
            {3, 9, 13, 18, 20},
            {4, 10, 14, 19, 21},
            {5, 11, 15, 20, 22},
            {6, 12, 21, 23},
            {7, 13, 22},
            {8, 2, 18, 25},
            {9, 3, 19, 24, 26},
            {10, 0, 4, 16, 20, 25, 27},
            {11, 1, 5, 17, 21, 26, 28},
            {12, 2, 6, 18, 22, 27, 29},
            {13, 3, 7, 19, 23, 28, 30},
            {14, 4, 20, 31},
            {15, 5, 21, 30},
            {16, 1, 10, 26, 33},
            {17, 0, 2, 11, 27, 32, 34},
            {18, 3, 8, 12, 24, 28, 33, 35},
            {19, 2, 4, 9, 13, 25, 29, 34, 36},
            {20, 3, 5, 10, 14, 26, 30, 35, 37},
            {21, 4, 6, 11, 15, 27, 31, 36, 38},
            {22, 5, 7, 12, 28, 37, 39},
            {23, 6, 13, 29, 38},
            {24, 9, 18, 34, 41},
            {25, 8, 10, 19, 35, 40, 42},
            {26, 9, 11, 16, 20, 32, 36, 41, 43},
            {27, 10, 12, 17, 21, 33, 37, 42, 44},
            {28, 11, 13, 18, 22, 34, 38, 43, 45},
            {29, 12, 14, 19, 23, 35, 39, 44, 46},
            {30, 13, 15, 20, 36, 45, 47},
            {31, 14, 21, 37, 46}
            // "upper" half of the board done,
            // "lower" half can easily be calculated
    };

    @Override
    protected int[][] moveSchemesUpperHalf() {
        return KNIGHT_MOVES_UPPER_HALF;
    }

    @Override
    protected void removeFriendlyFire(final List<Integer> moves, final byte[] board, final int myIndex) {
        final byte myColor = Piece.color(board[myIndex]);
        // only remove direct hits - knight can hop over friends
        moves.removeIf(i -> Piece.color(board[i]) == myColor);
    }
}
