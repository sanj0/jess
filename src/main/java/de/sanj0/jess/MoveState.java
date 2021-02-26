package de.sanj0.jess;

import java.util.ArrayList;
import java.util.List;

/**
 * The state of the user-made move.
 */
public class MoveState {
    private int hoveredSquare;
    private int draggedPieceIndex;
    private List<Integer> legalMoves;
    private byte draggedPiece;

    public MoveState(final int hoveredSquare, final int draggedPieceIndex, final List<Integer> legalMoves, final byte draggedPiece) {
        this.hoveredSquare = hoveredSquare;
        this.draggedPieceIndex = draggedPieceIndex;
        this.legalMoves = legalMoves;
        this.draggedPiece = draggedPiece;
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
