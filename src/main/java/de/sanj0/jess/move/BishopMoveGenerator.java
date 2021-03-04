package de.sanj0.jess.move;

import de.sanj0.jess.Piece;

import java.util.*;

public class BishopMoveGenerator extends MoveGenerator {

    private static final int[][] BISHOP_MOVES_UPPER_HALF = {
            {0, 9, 18, 27, 36, 45, 54, 63},
            {1, 8, 10, 19, 28, 37, 46, 55},
            {2, 9, 11, 16, 20, 29, 38, 47},
            {3, 10, 12, 17, 21, 24, 30, 39},
            {4, 11, 13, 18, 22, 25, 31, 32},
            {5, 12, 14, 19, 23, 26, 33, 40},
            {6, 13, 15, 20, 27, 34, 41, 48},
            {7, 14, 21, 28, 35, 42, 49, 56},
            {8, 1, 17, 26, 35, 44, 53, 62},
            {9, 0, 2, 16, 18, 27, 36, 45, 54, 63},
            {10, 1, 3, 17, 19, 24, 28, 37, 46, 55},
            {11, 2, 4, 18, 20, 25, 29, 32, 38, 47},
            {12, 3, 5, 19, 21, 26, 30, 33, 39, 40},
            {13, 4, 6, 20, 22, 27, 31, 34, 41, 48},
            {14, 5, 7, 21, 23, 28, 35, 42, 49, 56},
            {15, 6, 22, 29, 36, 43, 50, 57},
            {16, 2, 9, 25, 34, 43, 52, 61},
            {17, 3, 8, 10, 24, 26, 35, 44, 53, 62},
            {18, 0, 4, 9, 11, 25, 27, 32, 36, 45, 54, 63},
            {19, 1, 5, 10, 12, 26, 28, 33, 37, 40, 46, 55},
            {20, 2, 6, 11, 13, 27, 29, 34, 38, 41, 47, 48},
            {21, 3, 7, 12, 14, 28, 30, 35, 39, 42, 49, 56},
            {22, 4, 13, 15, 29, 31, 36, 43, 50, 57},
            {23, 5, 14, 30, 37, 44, 51, 58},
            {24, 3, 10, 17, 33, 42, 51, 60},
            {25, 4, 11, 16, 18, 32, 34, 43, 52, 61},
            {26, 5, 8, 12, 17, 19, 33, 35, 40, 44, 53, 62},
            {27, 0, 6, 9, 13, 18, 20, 34, 36, 41, 45, 48, 54, 63},
            {28, 1, 7, 10, 14, 19, 21, 35, 37, 42, 46, 49, 55, 56},
            {29, 2, 11, 15, 20, 22, 36, 38, 43, 47, 50, 57},
            {30, 3, 12, 21, 23, 37, 39, 44, 51, 58},
            {31, 4, 13, 22, 38, 45, 52, 59}
            //hallelujah
    };

    @Override
    public List<Integer> pseudoLegalMoves(final byte[] board, final int origin) {
        return filter0(super.pseudoLegalMoves(board, origin), board, origin);
    }

    @Override
    protected int[][] moveSchemesUpperHalf() {
        return BISHOP_MOVES_UPPER_HALF;
    }

    // filters out friendly fire and
    // sorts out captures, i.e. doesnt allow
    // to move past captures
    private List<Integer> filter0(final List<Integer> moves, final byte[] board, final int origin) {
        final List<Integer> filteredMoves = new ArrayList<>(moves.size());
        final byte myColor = Piece.color(board[origin]);
        final Set<Integer> blockedDiagonalIDs = new HashSet<>();
        // which square are the respective diagonals blocked from?
        final Map<Integer, Integer> blockedFrom = new HashMap<>();
        // enables us to handle captures the same way as friendly fire -
        // just store the captures to be retained here
        // key is the diagonal ID, value the capture
        final Map<Integer, Integer> capturesToRetain = new HashMap<>();
        for (final int m : moves) {
            if (board[m] != Piece.NONE) {
                final int distance = Math.abs(m - origin);
                final int d = normalizeDiagonal(origin, m);
                blockedDiagonalIDs.add(d);
                if (blockedFrom.getOrDefault(d, 100) > distance) {
                    // new nearer piece that blocks the diagonal
                    blockedFrom.put(d, distance);
                    if (Math.abs(capturesToRetain.getOrDefault(d, 100) - origin) > distance) {
                        // capture has to be removed, new one will be added
                        // directly after if the piece if an enemy
                        capturesToRetain.remove(d);
                    }
                    if (Piece.color(board[m]) != myColor) {
                        capturesToRetain.put(d, m);
                    }
                }
            }
        }

        for (final int m : moves) {
            final int normalizesDiagonal = normalizeDiagonal(origin, m);
            if (!blockedDiagonalIDs.contains(normalizesDiagonal) || blockedFrom.getOrDefault(normalizesDiagonal, -100) > Math.abs(m - origin)) {
                // diagonal is free, just go ahead!
                filteredMoves.add(m);
            }
        }

        filteredMoves.addAll(capturesToRetain.values());

        return filteredMoves;
    }

    private int normalizeDiagonal(final int origin, final int dst) {
        int d = dst - origin;
        if (d % 7 == 0) {
            return d > 0 ? 7 : -7;
        } else if (d % 9 == 0) {
            return d > 0 ? 9 : -9;
        }
        // can never be reached anyways
        return -1;
    }

    @Override
    protected void removeFriendlyFire(final List<Integer> moves, final byte[] board, final int myIndex) {
        // nothing to do
    }
}
