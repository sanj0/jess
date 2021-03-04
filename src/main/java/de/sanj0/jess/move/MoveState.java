package de.sanj0.jess.move;

import de.sanj0.jess.Arrow;
import de.sanj0.jess.SquareMark;

import java.util.ArrayList;
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

    private final List<Arrow> arrows = new ArrayList<>();
    private final List<SquareMark> squareMarks = new ArrayList<>();

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

    public void toggleSquareMark(final SquareMark m) {
        if (squareMarks.contains(m)) {
            squareMarks.removeIf(a -> a.equals(m));
        } else {
            squareMarks.add(m);
        }
    }

    public void toggleArrow(final Arrow arrow) {
        if (arrows.contains(arrow)) {
            arrows.removeIf(a -> a.equals(arrow));
        } else {
            arrows.add(arrow);
        }
    }

    /**
     * Gets {@link #arrows}.
     *
     * @return the value of {@link #arrows}
     */
    public List<Arrow> getArrows() {
        return arrows;
    }

    /**
     * Gets {@link #squareMarks}.
     *
     * @return the value of {@link #squareMarks}
     */
    public List<SquareMark> getSquareMarks() {
        return squareMarks;
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
