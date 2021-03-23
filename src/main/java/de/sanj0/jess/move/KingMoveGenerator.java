package de.sanj0.jess.move;

import de.sanj0.jess.Piece;

import java.util.List;

public class KingMoveGenerator extends MoveGenerator {

    private static final int[][] KING_MOVES_UPPER_HALF = {
            {0, 1, 8, 9},
            {1, 0, 2, 8, 9, 10},
            {2, 1, 3, 9, 10, 11},
            {3, 2, 4, 10, 11, 12},
            {4, 3, 5, 11, 12, 13},
            {5, 4, 6, 12, 13, 14},
            {6, 5, 7, 13, 14, 15},
            {7, 6, 14, 15},
            {8, 0, 1, 9, 16, 17},
            {9, 0, 1, 2, 8, 10, 16, 17, 18},
            {10, 1, 2, 3, 9, 11, 17, 18, 19},
            {11, 2, 3, 4, 10, 12, 18, 19, 20},
            {12, 3, 4, 5, 11, 13, 19, 20, 21},
            {13, 4, 5, 6, 12, 14, 20, 21, 22},
            {14, 5, 6, 7, 13, 15, 21, 22, 23},
            {15, 6, 7, 14, 22, 23},
            {16, 8, 9, 17, 24, 25},
            {17, 8, 9, 10, 16, 18, 24, 25, 26},
            {18, 9, 10, 11, 17, 19, 25, 26, 27},
            {19, 10, 11, 12, 18, 20, 26, 27, 28},
            {20, 11, 12, 13, 19, 21, 27, 28, 29},
            {21, 12, 13, 14, 20, 22, 28, 29, 30},
            {22, 13, 14, 15, 21, 23, 29, 30, 31},
            {23, 14, 15, 22, 30, 31},
            {24, 16, 17, 25, 32, 33},
            {25, 16, 17, 18, 24, 26, 32, 33, 34},
            {26, 17, 18, 19, 25, 27, 33, 34, 35},
            {27, 18, 19, 20, 26, 28, 34, 35, 36},
            {28, 19, 20, 21, 27, 29, 35, 36, 37},
            {29, 20, 21, 22, 28, 30, 36, 37, 38},
            {30, 21, 22, 23, 29, 31, 37, 38, 39},
            {31, 22, 23, 30, 38, 39}
    };

    @Override
    protected int[][] moveSchemesUpperHalf() {
        return KING_MOVES_UPPER_HALF;
    }

    @Override
    protected void removeFriendlyFire(final List<Integer> moves, final byte[] board, final int myIndex) {
        final byte myColor = Piece.color(board[myIndex]);
        moves.removeIf(i -> Piece.color(board[i]) == myColor);
        // add castle moves - no matter the rights atm
        if (myColor == Piece.LIGHT) {
            moves.add(CastleMove.LIGHT_KING_SIDE_CASTLE);
            moves.add(CastleMove.LIGHT_QUEEN_SIDE_CASTLE);
        } else {
            moves.add(CastleMove.DARK_KING_SIDE_CASTLE);
            moves.add(CastleMove.DARK_QUEEN_SIDE_CASTLE);
        }
    }
}
