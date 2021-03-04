package de.sanj0.jess.move;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class DummyMoveGenerator extends MoveGenerator {

    @Override
    public List<Integer> pseudoLegalMoves(final byte[] board, final int origin) {
        return IntStream.rangeClosed(0, 63).boxed().collect(Collectors.toList());
    }

    @Override
    protected int[][] moveSchemesUpperHalf() {
        return new int[0][];
    }

    @Override
    protected void removeFriendlyFire(final List<Integer> moves, final byte[] board, final int myIndex) {
        // nothing to do ¯\_(ツ)_/¯
    }
}
