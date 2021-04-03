package de.sanj0.jess.move;

import de.sanj0.jess.Piece;

import java.util.*;

/**
 * Moves
 */
public class Moves {

    private static MoveGenerator PAWN_MOVES = new PawnMoveGenerator();
    private static MoveGenerator KNIGHT_MOVES = new KnightMoveGenerator();
    private static MoveGenerator BISHOP_MOVES = new BishopMoveGenerator();
    private static MoveGenerator ROOK_MOVES = new RookMoveGenerator();
    private static MoveGenerator QUEEN_MOVES = new QueenMoveGenerator();
    private static MoveGenerator KING_MOVES = new KingMoveGenerator();

    private static Map<Byte, MoveGenerator> MOVE_GENERATORS = new HashMap<Byte, MoveGenerator>() {{
        put(Piece.PAWN, PAWN_MOVES);
        put(Piece.KNIGHT, KNIGHT_MOVES);
        put(Piece.BISHOP, BISHOP_MOVES);
        put(Piece.ROOK, ROOK_MOVES);
        put(Piece.QUEEN, QUEEN_MOVES);
        put(Piece.KING, KING_MOVES);
    }};

    public static Move move(final byte[] position, final int startIndex, final int endIndex) {
        if (isCastle(position, startIndex, endIndex)) {
            switch (endIndex) {
                case CastleMove.LIGHT_KING_SIDE_CASTLE:
                    return CastleMove.castle(CastleMove.CastleType.KING_SIDE_LIGHT);
                case CastleMove.LIGHT_QUEEN_SIDE_CASTLE:
                    return CastleMove.castle(CastleMove.CastleType.QUEEN_SIDE_LIGHT);
                case CastleMove.DARK_KING_SIDE_CASTLE:
                    return CastleMove.castle(CastleMove.CastleType.KING_SIDE_DARK);
                case CastleMove.DARK_QUEEN_SIDE_CASTLE:
                    return CastleMove.castle(CastleMove.CastleType.QUEEN_SIDE_DARK);
            }
        }
        final int[] indices = {startIndex, endIndex};
        final byte[] before = {position[startIndex], position[endIndex]};
        final byte[] after = {Piece.NONE, position[startIndex]};
        return new Move(indices, before, after);
    }

    private static boolean isCastle(final byte[] position, final int start, final int end) {
        if (Piece.isKing(position[start])) {
            // check if the king is still in its original position
            return Piece.startingIndex(position[start]).contains(start);
        }
        return false;
    }

    private static List<Integer> allPseudoLegalMoves(final byte[] board, final byte color) {
        final List<Integer> moves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            byte b = board[i];
            if (b != Piece.NONE && Piece.color(b) == color) {
                moves.addAll(pseudoLegalMoves(board, i));
            }
        }

        return moves;
    }

    public static List<Move> allLegalMoves(final byte[] board, final MoveState moveState) {
        final List<Move> moves = new ArrayList<>();
        for (int i = 0; i < board.length; i++) {
            byte b = board[i];
            if (b != Piece.NONE && Piece.color(b) == moveState.getColorToMove()) {
                final List<Integer> mList = legalMoves(board, i, moveState);
                for (final int m : mList) {
                    moves.add(move(board, i, m));
                }
            }
        }

        return moves;
    }

    // pseudo legal moves for the piece at the given index
    // doesnt contain castles
    private static List<Integer> pseudoLegalMoves(final byte[] board, final int position) {
        final byte piece = board[position];
        if (piece == Piece.NONE) return new ArrayList<>();
        final byte type = Piece.type(piece);
        final MoveGenerator generator = MOVE_GENERATORS.get(type);
        List<Integer> pseudoLegal = generator.pseudoLegalMoves(board, position);
        generator.removeFriendlyFire(pseudoLegal, board, position);

        return pseudoLegal;
    }

    public static List<Integer> legalMoves(final byte[] board, final int position, final MoveState moveState) {
        final byte myColor = Piece.color(board[position]);

        if (moveState.getColorToMove() != myColor)
            return new ArrayList<>();

        final List<Integer> legalMoves = new ArrayList<>();
        final List<Integer> pseudolegalMoves = pseudoLegalMoves(board, position);
        final byte enemyColor = Piece.oppositeColor(myColor);
        final int myKing = kingPosition(board, myColor);

        for (final int m : pseudolegalMoves) {
            final List<Integer> allResponses = allPseudoLegalMoves(move(board, position, m).boardAfterMove(board), enemyColor);
            boolean addMove = true;
            for (final int r : allResponses) {
                if (r == (myKing == position ? m : myKing)) {
                    addMove = false;
                    break;
                }
            }
            if (addMove)
                legalMoves.add(m);
        }

        return legalMoves;
    }

    // optimization by different search start depending on color?
    private static int kingPosition(final byte[] board, final byte color) {
        final byte k = Piece.get(Piece.KING, color);
        for (int i = 0; i < board.length; i++) {
            if (board[i] == k) {
                return i;
            }
        }

        return -1;
    }

    public static int freeSquaresUp(final int position) {
        return position / 8;
    }

    public static int freeSquaresDown(final int position) {
        return 7 - freeSquaresUp(position);
    }

    public static int freeSquaresRight(final int position) {
        return (position % 8) - 1;
    }

    public static int freeSquaresLeft(final int position) {
        return 7 - freeSquaresRight(position);
    }
}
