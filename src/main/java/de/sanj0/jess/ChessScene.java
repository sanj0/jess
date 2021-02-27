package de.sanj0.jess;

import de.edgelord.saltyengine.input.Input;
import de.edgelord.saltyengine.input.MouseInputAdapter;
import de.edgelord.saltyengine.scene.Scene;

import java.awt.event.MouseEvent;

public class ChessScene extends Scene {

    private final Board board;
    private final BoardRenderer boardRenderer;

    public ChessScene(final Board board) {
        this.board = board;
        boardRenderer = new BoardRenderer(board);
    }

    @Override
    public void initialize() {
        addDrawingRoutine(boardRenderer);

        Input.addMouseInputHandler(new MouseInputAdapter() {
            @Override
            public void mousePressed(final MouseEvent e) {
                final MoveState moveState = boardRenderer.getMoveState();
                final int draggedPieceIndex = BoardRenderer.indexOfPosition(e.getX(), e.getY());
                moveState.setDraggedPieceIndex(draggedPieceIndex);
                moveState.setDraggedPiece(board.getPosition()[draggedPieceIndex]);
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                final MoveState moveState = boardRenderer.getMoveState();
                // check if square is legal and make the move or
                // move back to origin
                final int position = BoardRenderer.indexOfPosition(e.getX(), e.getY());
                if (position != moveState.getDraggedPieceIndex()) {
                    board.getPosition()[BoardRenderer.indexOfPosition(e.getX(), e.getY())] = moveState.getDraggedPiece();
                    board.getPosition()[moveState.getDraggedPieceIndex()] = Piece.NONE;
                }
                moveState.setDraggedPieceIndex(-1);
                moveState.setDraggedPiece(Piece.NONE);
            }

            @Override
            public void mouseMoved(final MouseEvent e) {
                boardRenderer.getMoveState().setHoveredSquare(BoardRenderer.indexOfPosition(e.getX(), e.getY()));
            }
        });
    }
}
