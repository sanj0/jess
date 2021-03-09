package de.sanj0.jess.move;

import de.sanj0.jess.Piece;

public class CastleMove extends Move {

    private final CastleType type;
    private final byte king;
    private final byte rook;
    public enum CastleType {
        // king: 60-62; rook: 63-61
        KING_SIDE_LIGHT,
        // king: 4-6; rook: 7-5
        KING_SIDE_DARK,
        // king: 60-58; rook: 56-59
        QUEEN_SIDE_LIGHT,
        // king: 4-2; 0-3
        QUEEN_SIDE_DARK
    }

    private CastleMove(final CastleType type) {
        super(null, null, null);
        this.type = type;
        if (type == CastleType.KING_SIDE_LIGHT || type == CastleType.QUEEN_SIDE_LIGHT) {
            king = Piece.get(Piece.KING, Piece.LIGHT);
            rook = Piece.get(Piece.ROOK, Piece.LIGHT);
        } else {
            king = Piece.get(Piece.KING, Piece.DARK);
            rook = Piece.get(Piece.ROOK, Piece.DARK);
        }
    }

    public static CastleMove castle(final CastleType castleType) {
        return new CastleMove(castleType);
    }

    private int[] kingMove() {
        switch (type) {
            case KING_SIDE_LIGHT:
                return new int[]{60, 62};
            case KING_SIDE_DARK:
                return new int[]{4, 6};
            case QUEEN_SIDE_LIGHT:
                return new int[]{60, 58};
            case QUEEN_SIDE_DARK:
                return new int[]{4, 2};
        }
        return new int[0];
    }

    private int[] rookMove() {
        switch (type) {
            case KING_SIDE_LIGHT:
                return new int[]{63, 61};
            case KING_SIDE_DARK:
                return new int[]{7, 5};
            case QUEEN_SIDE_LIGHT:
                return new int[]{56, 59};
            case QUEEN_SIDE_DARK:
                return new int[]{0, 3};
        }
        return new int[0];
    }

    @Override
    public byte[] boardAfterMove(final byte[] position) {
        final byte[] board = new byte[position.length];
        final int[] kingMove = kingMove();
        final int[] rookMove = rookMove();
        for (int i = 0; i < position.length; i++) {
            if (i == kingMove[0] || i == rookMove[0]) {
                board[i] = Piece.NONE;
            } else if (i == kingMove[1]) {
                board[i] = king;
            } else if (i == rookMove[1]) {
                board[i] = rook;
            }else {
                board[i] = position[i];
            }
        }

        return board;
    }

    @Override
    public int rating(final byte[] board) {
        return type == CastleType.KING_SIDE_DARK || type == CastleType.KING_SIDE_LIGHT
                ? 10 : 7;
    }

    @Override
    public void doMove(final byte[] board) {
        final int[] kingMove = kingMove();
        final int[] rookMove = rookMove();
        board[kingMove[1]] = king;
        board[kingMove[0]] = Piece.NONE;
        board[rookMove[1]] = rook;
        board[rookMove[0]] = Piece.NONE;
    }

    @Override
    public void undoMove(final byte[] board) {
        final int[] kingMove = kingMove();
        final int[] rookMove = rookMove();
        board[kingMove[0]] = king;
        board[kingMove[1]] = Piece.NONE;
        board[rookMove[0]] = rook;
        board[rookMove[1]] = Piece.NONE;
    }

    @Override
    public String notation() {
        return type == CastleType.KING_SIDE_DARK || type == CastleType.KING_SIDE_LIGHT
                ? "O-O" : "O-O-O";
    }
}
