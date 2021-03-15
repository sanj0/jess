package de.sanj0.jess;

import de.sanj0.jess.move.Move;
import de.sanj0.jess.move.MoveState;
import de.sanj0.jess.move.Moves;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ChessAI {

    public static final int DEPTH = 3;

    private static final Random rng = new SecureRandom();
    public static Move move(final byte[] board, final MoveState moveState) {
        final List<Move> allLegalMoves = Moves.allLegalMoves(board, moveState);
        if (allLegalMoves.isEmpty()) {
            System.out.println(moveState.getColorToMove() == Piece.LIGHT ? "white" : "black" + " won the game!");
            return Moves.move(board, 0, 0);
        } else {
            int maxRating = Integer.MIN_VALUE;
            List<Move> bestMoves = new ArrayList<>();
            for (final Move m : allLegalMoves) {
                final int rating = rateMove(DEPTH, m, board, moveState);
                if (rating > maxRating) {
                    System.out.println(m.notation() + " = " + rating + " which is a new max.");
                    maxRating = rating;
                    bestMoves.clear();
                    bestMoves.add(m);
                } else if (rating == maxRating) {
                    bestMoves.add(m);
                    System.out.println(m.notation() + " = " + rating + " and thus adds to the best moves.");
                } else {
                    System.out.println("bad move: " + m.notation() + " = " + rating);
                }
            }
            System.out.println("---");
            final Move m = bestMoves.get(rng.nextInt(bestMoves.size()));
            m.doMove(board);
            moveState.nextTurn();
            return m;
        }
    }

    private static int rateMove(final int depth, final Move m, final byte[] board, final MoveState moveState) {
        int rating = m.rating(board);
        if (depth == 0) {
            return rating;
        }
        byte[] boardAfter = m.boardAfterMove(board);
        MoveState mState = moveState.colorToMove(Piece.oppositeColor(moveState.getColorToMove()));
        final List<Move> allResponses = Moves.allLegalMoves(boardAfter, mState);

        if (allResponses.isEmpty()) {
            if ((DEPTH - depth) % 2 == 1) {
                return Integer.MIN_VALUE + 1;
            } else {
                return Integer.MAX_VALUE;
            }
        }

        int bestResponseRating = 0;
        for (final Move response : allResponses) {
            final int responseRating = rateMove(depth - 1, response, boardAfter, mState);
            if (bestResponseRating < responseRating) {
                bestResponseRating = responseRating;
            }
        }
        return rating - bestResponseRating;
    }
}
