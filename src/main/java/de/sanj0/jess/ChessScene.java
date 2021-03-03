package de.sanj0.jess;

import de.edgelord.saltyengine.input.Input;
import de.edgelord.saltyengine.input.KeyboardInputAdapter;
import de.edgelord.saltyengine.input.MouseInputAdapter;
import de.edgelord.saltyengine.scene.Scene;

import java.awt.event.KeyEvent;
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
                if (e.getButton() == MouseEvent.BUTTON1) {
                    final MoveState moveState = boardRenderer.getMoveState();
                    final int draggedPieceIndex = BoardRenderer.indexOfPosition(e.getX(), e.getY());
                    moveState.setDraggedPieceIndex(draggedPieceIndex);
                    moveState.setDraggedPiece(board.getPosition()[draggedPieceIndex]);
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    final MoveState moveState = boardRenderer.getMoveState();
                    // check if square is legal and make the move or
                    // move back to origin
                    final int position = BoardRenderer.indexOfPosition(e.getX(), e.getY());
                    if (position != moveState.getDraggedPieceIndex()) {
                        final Move move = Moves.move(board.getPosition(), moveState.getDraggedPieceIndex(), BoardRenderer.indexOfPosition(e.getX(), e.getY()));
                        move.doMove(board.getPosition());
                    }
                    moveState.setDraggedPieceIndex(-1);
                    moveState.setDraggedPiece(Piece.NONE);
                }
            }

            @Override
            public void mouseMoved(final MouseEvent e) {
                boardRenderer.getMoveState().setHoveredSquare(BoardRenderer.indexOfPosition(e.getX(), e.getY()));
            }
        });

        Input.addKeyboardInputHandler(new KeyboardInputAdapter() {
            @Override
            public void keyTyped(final KeyEvent e) {
                if (e.getKeyChar() == 'r') {
                    // reset position
                    board.setPosition(BoardPositions.parseFEN(Main.STARTING_FEN));
                }
            }
        });
    }
}
