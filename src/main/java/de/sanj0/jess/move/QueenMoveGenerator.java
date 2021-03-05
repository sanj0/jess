package de.sanj0.jess.move;

import java.util.ArrayList;
import java.util.List;

public class QueenMoveGenerator extends MoveGenerator {

    private RookMoveGenerator rookMoves = new RookMoveGenerator();
    private BishopMoveGenerator bishopMoves = new BishopMoveGenerator();

    @Override
    public List<Integer> pseudoLegalMoves(final byte[] board, final int origin) {
        final List<Integer> moves = new ArrayList<>();
        moves.addAll(rookMoves.pseudoLegalMoves(board, origin));
        moves.addAll(bishopMoves.pseudoLegalMoves(board, origin));
        return moves;
    }

    @Override
    protected int[][] moveSchemesUpperHalf() {
        return new int[0][];
    }

    @Override
    protected void removeFriendlyFire(final List<Integer> moves, final byte[] board, final int myIndex) {

    }
}
