package de.sanj0.jess.move;

import de.sanj0.jess.JessUtils;

import java.util.*;

public abstract class MoveGenerator {

    private final Map<Integer, List<Integer>> moveMap = new HashMap<>();

    public MoveGenerator() {
        initializeMap();
    }

    protected abstract int[][] moveSchemesUpperHalf();
    protected abstract void removeFriendlyFire(final List<Integer> moves, final byte[] board, final int myIndex);

    public List<Integer> pseudoLegalMoves(final byte[] board, final int origin) {
        return new ArrayList<>(moveMap.get(origin));
    }

    protected void initializeMap() {
        for (final int[] moves : moveSchemesUpperHalf()) {
            final int origin = moves[0];
            final List<Integer> dst = JessUtils.intArrayToList(moves, 1, moves.length);
            // might be useful to have the lists in descending order
            Collections.reverse(dst);
            moveMap.put(origin, dst);
            // put mirrored move set for "lower" half of the board
            final int mirroredOrigin = 63 - origin;
            moveMap.put(mirroredOrigin, mirrorMoves(origin, mirroredOrigin, dst));
        }
    }

    private static List<Integer> mirrorMoves(final int origin, final int mirroredOrigin, final List<Integer> dst) {
        final List<Integer> mirrorDst = new ArrayList<>(dst.size());
        for (final int move : dst) {
            mirrorDst.add(origin - move + mirroredOrigin);
        }
        return mirrorDst;
    }
}
