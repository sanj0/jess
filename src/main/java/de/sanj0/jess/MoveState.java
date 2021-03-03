package de.sanj0.jess;

import java.util.List;
import java.util.Stack;

/**
 * The state of the user-made move.
 */
public class MoveState {
    private int hoveredSquare;
    private int draggedPieceIndex;
    private List<Integer> legalMoves;
    private byte draggedPiece;

    private final Stack<Move> moveStack = new Stack<>();
    private final Stack<Move> redoStack = new Stack<>();

    public MoveState(final int hoveredSquare, final int draggedPieceIndex,
                     final List<Integer> legalMoves, final byte draggedPiece) {
        this.hoveredSquare = hoveredSquare;
        this.draggedPieceIndex = draggedPieceIndex;
        this.legalMoves = legalMoves;
        this.draggedPiece = draggedPiece;
    }

    public void pushMove(final Move move) {
        moveStack.push(move);
    }

    public void undoMove(final byte[] board) {
        if (!moveStack.isEmpty()) {
            final Move move = moveStack.pop();
            move.undoMove(board);
            redoStack.push(move);
        }
    }

    public void redoMove(final byte[] board) {
        if (!redoStack.isEmpty()) {
            final Move move = redoStack.pop();
            move.doMove(board);
            moveStack.push(move);
        }
    }

    public void clearMoveStacks() {
        moveStack.clear();
        redoStack.clear();
    }

    /**
     * Gets {@link #moveStack}.
     *
     * @return the value of {@link #moveStack}
     */
    public Stack<Move> getMoveStack() {
        return moveStack;
    }

    /**
     * Gets {@link #redoStack}.
     *
     * @return the value of {@link #redoStack}
     */
    public Stack<Move> getRedoStack() {
        return redoStack;
    }

    /**
     * Gets {@link #hoveredSquare}.
     *
     * @return the value of {@link #hoveredSquare}
     */
    public int getHoveredSquare() {
        return hoveredSquare;
    }

    /**
     * Sets {@link #hoveredSquare}.
     *
     * @param hoveredSquare the new value of {@link #hoveredSquare}
     */
    public void setHoveredSquare(final int hoveredSquare) {
        this.hoveredSquare = hoveredSquare;
    }

    /**
     * Gets {@link #draggedPieceIndex}.
     *
     * @return the value of {@link #draggedPieceIndex}
     */
    public int getDraggedPieceIndex() {
        return draggedPieceIndex;
    }

    /**
     * Sets {@link #draggedPieceIndex}.
     *
     * @param draggedPieceIndex the new value of {@link #draggedPieceIndex}
     */
    public void setDraggedPieceIndex(final int draggedPieceIndex) {
        this.draggedPieceIndex = draggedPieceIndex;
    }

    /**
     * Gets {@link #legalMoves}.
     *
     * @return the value of {@link #legalMoves}
     */
    public List<Integer> getLegalMoves() {
        return legalMoves;
    }

    /**
     * Sets {@link #legalMoves}.
     *
     * @param legalMoves the new value of {@link #legalMoves}
     */
    public void setLegalMoves(final List<Integer> legalMoves) {
        this.legalMoves = legalMoves;
    }

    /**
     * Gets {@link #draggedPiece}.
     *
     * @return the value of {@link #draggedPiece}
     */
    public byte getDraggedPiece() {
        return draggedPiece;
    }

    /**
     * Sets {@link #draggedPiece}.
     *
     * @param draggedPiece the new value of {@link #draggedPiece}
     */
    public void setDraggedPiece(final byte draggedPiece) {
        this.draggedPiece = draggedPiece;
    }
}
