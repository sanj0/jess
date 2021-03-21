package de.sanj0.jess;

import de.sanj0.jess.move.Move;
import de.sanj0.jess.move.MoveState;
import de.sanj0.jess.move.Moves;

import java.security.SecureRandom;
import java.util.*;

public class ChessAI {

    public static final int DEPTH = 2;

    private static final Random rng = new SecureRandom();
    public static Move move(final byte[] board, final MoveState moveState) {
        final List<Move> allLegalMoves = Moves.allLegalMoves(board, moveState);
        if (allLegalMoves.isEmpty()) {
            System.out.println((moveState.getColorToMove() != Piece.LIGHT ? "white" : "black") + " won the game!");
            return Moves.move(board, 0, 0);
        } else {
            int maxRating = Integer.MIN_VALUE;
            final List<Move> bestMoveCandidates = new ArrayList<>();
            final HashMap<Move, Integer> mobilityScores = new HashMap<>();
            for (final Move m : allLegalMoves) {
                final int[] rating = rateMove(DEPTH, m, board, moveState);
                final int score = rating[0];
                if (score > maxRating) {
                    System.out.println(m.notation() + " = " + score + " which is a new max.");
                    maxRating = score;
                    bestMoveCandidates.clear();
                    mobilityScores.clear();
                    bestMoveCandidates.add(m);
                    mobilityScores.put(m, rating[1]);
                } else if (score == maxRating) {
                    bestMoveCandidates.add(m);
                    mobilityScores.put(m, rating[1]);
                    System.out.println(m.notation() + " = " + score + " and thus adds to the best moves.");
                } else {
                    System.out.println("bad move: " + m.notation() + " = " + score);
                }
            }
            final int candidatesBefore = bestMoveCandidates.size();
            final int highestMobilityScore = mobilityScores.values().stream().mapToInt(i -> i).max().getAsInt();
            System.out.println("highest mobility rating: " + highestMobilityScore);
            bestMoveCandidates.removeIf(m -> mobilityScores.getOrDefault(m, 0) != highestMobilityScore);
            System.out.println("purged " + (candidatesBefore - bestMoveCandidates.size()) + " move candidates due to insufficient mobility");
            System.out.println(bestMoveCandidates.size() + " moves remaining to choose from...");
            System.out.println("---");
            final Move m = bestMoveCandidates.get(rng.nextInt(bestMoveCandidates.size()));
            m.doMove(board);
            moveState.nextTurn();
            return m;
        }
    }

    // returns array:
    // {(move rating), (mobility score -> number of possible follow ups)}
    private static int[] rateMove(final int depth, final Move m, final byte[] board, final MoveState moveState) {
        int rating = m.rating(board);
        byte[] boardAfter = m.boardAfterMove(board);
        final List<Move> possibleFollowUps = Moves.allLegalMoves(boardAfter, moveState);
        if (depth == 0) {
            return new int[] {rating, possibleFollowUps.size()};
        }
        MoveState mState = moveState.colorToMove(Piece.oppositeColor(moveState.getColorToMove()));
        final List<Move> allResponses = Moves.allLegalMoves(boardAfter, mState);

        if (allResponses.isEmpty()) {
            if ((DEPTH - depth) % 2 == 1) {
                return new int[] {Integer.MIN_VALUE + 1, 0};
            } else {
                return new int[] {Integer.MAX_VALUE, 0};
            }
        }

        int bestResponseRating = 0;
        for (final Move response : allResponses) {
            final int[] responseRating = rateMove(depth - 1, response, boardAfter, mState);
            if (bestResponseRating < responseRating[0]) {
                bestResponseRating = responseRating[0];
            }
        }
        return new int[] {rating - bestResponseRating, possibleFollowUps.size()};
    }
}
