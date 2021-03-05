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
    private static MoveGenerator KING_MOVES = new KnightMoveGenerator();

    private static Map<Byte, MoveGenerator> MOVE_GENERATORS = new HashMap<Byte, MoveGenerator>() {{
        put(Piece.PAWN, PAWN_MOVES);
        put(Piece.KNIGHT, KNIGHT_MOVES);
        put(Piece.BISHOP, BISHOP_MOVES);
        put(Piece.ROOK, ROOK_MOVES);
        put(Piece.QUEEN, QUEEN_MOVES);
        put(Piece.KING, KING_MOVES);
    }};

    public static Move move(final byte[] position, final int startIndex, final int endIndex) {
        final int[] indices = {startIndex, endIndex};
        final byte[] before = {position[startIndex], position[endIndex]};
        final byte[] after = {Piece.NONE, position[startIndex]};
        return new Move(indices, before, after);
    }

    // pseudo legal moves for the piece at the given index
    public static List<Integer> pseudoLegalMoves(final byte[] board, final int position) {
        final byte piece = board[position];
        if (piece == Piece.NONE) return new ArrayList<>();
        final byte type = Piece.type(piece);
        final MoveGenerator generator = MOVE_GENERATORS.get(type);
        List<Integer> pseudoLegal = generator.pseudoLegalMoves(board, position);
        generator.removeFriendlyFire(pseudoLegal, board, position);

        return pseudoLegal;
    }

    private static List<Integer> kingMoves(final int position) {
        return new ArrayList<>(Arrays.asList(
                position - 9, position - 8, position - 7, position - 1, position + 1, position + 7, position + 8, position + 9));
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
