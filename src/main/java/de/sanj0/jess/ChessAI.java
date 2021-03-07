package de.sanj0.jess;

import de.sanj0.jess.move.Move;
import de.sanj0.jess.move.MoveState;
import de.sanj0.jess.move.Moves;

import java.security.SecureRandom;
import java.util.*;

public class ChessAI {

    private static final Random rng = new SecureRandom();
    public static Move move(final byte[] board, final MoveState moveState) {
        final List<Move> allLegalMoves = Moves.allLegalMoves(board, moveState);
        if (allLegalMoves.isEmpty()) {
            System.out.println("white won the game");
            return Moves.move(board, 0, 0);
        } else {
            allLegalMoves.sort(new Depth1MoveComparator(board, moveState));
            final Move m = allLegalMoves.get(allLegalMoves.size() - 1);
            m.doMove(board);
            moveState.nextTurn();
            return m;
        }
    }

    private static class Depth1MoveComparator implements Comparator<Move> {
        private final byte[] board;
        private final MoveState moveState;

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
            } else if (allResponses2.isEmpty()) {
                rating2 = Integer.MAX_VALUE;
            }
            rating1 -= allResponses1.get(allResponses1.size() - 1).rating(boardAfterM1);
            rating2 -= allResponses2.get(allResponses2.size() - 1).rating(boardAfterM2);

            /*
            System.out.println(m1.notation() + " - best response: " + allResponses1.get(allResponses1.size() - 1).rating(boardAfterM1)
                    + " / fearing " + allResponses1.get(allResponses1.size() - 1).notation());
            System.out.println(m2.notation() + " - best response: " + allResponses2.get(allResponses2.size() - 1).rating(boardAfterM2)
                    + " / fearing " + allResponses2.get(allResponses2.size() - 1).notation());*/
            return Integer.compare(rating1, rating2);
        }
    }
}
