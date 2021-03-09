package de.sanj0.jess;

import de.sanj0.jess.move.Move;
import de.sanj0.jess.move.MoveState;
import de.sanj0.jess.move.Moves;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class ChessAI {

    public static final int DEPTH = 2;

    private static final Random rng = new SecureRandom();
    public static Move move(final byte[] board, final MoveState moveState) {
        final List<Move> allLegalMoves = Moves.allLegalMoves(board, moveState);
        if (allLegalMoves.isEmpty()) {
            System.out.println("white won the game");
            return Moves.move(board, 0, 0);
        } else {
            allLegalMoves.sort(new Depth1MoveComparator(board, moveState));
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
                }
                System.out.println("bad move: " + m.notation() + " = " + rating);
            }
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

    private static class Depth1MoveComparator implements Comparator<Move> {
        private final byte[] board;
        private final MoveState moveState;
        private final List<Move> highestRated = new ArrayList<>();
        private int highestRating = 0;

        public Depth1MoveComparator(final byte[] board, final MoveState moveState) {
            this.board = board;
            this.moveState = moveState;
        }

        @Override
        public int compare(final Move m1, final Move m2) {
            int rating1 = m1.rating(board);
            int rating2 = m2.rating(board);
            /*System.out.println(m1.notation() + " : " + m1.rating(board));
            System.out.println(m2.notation() + " : " + m2.rating(board));*/
            byte[] boardAfterM1 = m1.boardAfterMove(board);
            byte[] boardAfterM2 = m2.boardAfterMove(board);
            final MoveState responseState = moveState.colorToMove(Piece.oppositeColor(moveState.getColorToMove()));
            final List<Move> allResponses1 = Moves.allLegalMoves(boardAfterM1, responseState);
            final List<Move> allResponses2 = Moves.allLegalMoves(boardAfterM2, responseState);

            allResponses1.sort(Comparator.comparingInt(m -> m.rating(boardAfterM1)));
            allResponses2.sort(Comparator.comparingInt(m -> m.rating(boardAfterM2)));
            if (allResponses1.isEmpty()) {
                rating1 = Integer.MAX_VALUE;
                System.out.println(m1.notation() + " = " + rating1);
                return Integer.compare(rating1, rating2);
            } else {
                rating1 -= allResponses1.get(allResponses1.size() - 1).rating(boardAfterM1);
            }

            if (allResponses2.isEmpty()) {
                rating2 = Integer.MAX_VALUE;
                System.out.println(m2.notation() + " = " + rating2);
                return Integer.compare(rating1, rating2);
            } else {
                rating2 -= allResponses2.get(allResponses2.size() - 1).rating(boardAfterM2);
            }
            if (rating2 > rating1) {
                if (highestRating <= rating2) {
                    highestRating = rating2;
                    highestRated.clear();
                    highestRated.add(m2);
                }
            }

            /*
            System.out.println(m1.notation() + " - best response: " + allResponses1.get(allResponses1.size() - 1).rating(boardAfterM1)
                    + " / fearing " + allResponses1.get(allResponses1.size() - 1).notation());
            System.out.println(m2.notation() + " - best response: " + allResponses2.get(allResponses2.size() - 1).rating(boardAfterM2)
                    + " / fearing " + allResponses2.get(allResponses2.size() - 1).notation());*/
            return Integer.compare(rating1, rating2);
        }
    }
}
