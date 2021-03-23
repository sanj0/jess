package de.sanj0.jess;

import de.edgelord.saltyengine.input.Input;
import de.edgelord.saltyengine.input.KeyboardInputAdapter;
import de.edgelord.saltyengine.input.MouseInputAdapter;
import de.edgelord.saltyengine.scene.Scene;
import de.edgelord.saltyengine.transform.Vector2f;
import de.sanj0.jess.marks.Arrow;
import de.sanj0.jess.marks.SquareMark;
import de.sanj0.jess.move.CastleMove;
import de.sanj0.jess.move.Move;
import de.sanj0.jess.move.MoveState;
import de.sanj0.jess.move.Moves;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class ChessScene extends Scene {

    private final Board board;
    private final BoardRenderer boardRenderer;

    private boolean autoMove = false;

    public ChessScene(final Board board) {
        this.board = board;
        boardRenderer = new BoardRenderer(board);
    }

    @Override
    public void initialize() {
        addDrawingRoutine(boardRenderer);

        Input.addMouseInputHandler(new MouseInputAdapter() {
            private Vector2f arrowStart = null;

            @Override
            public void mouseClicked(final MouseEvent e) {
                // remove all marks on double click of right mouse btn
                if (e.getButton() == MouseEvent.BUTTON3 && e.getClickCount() == 2) {
                    boardRenderer.getMoveState().getSquareMarks().clear();
                    boardRenderer.getMoveState().getArrows().clear();
                }
            }

            @Override
            public void mousePressed(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    final MoveState moveState = boardRenderer.getMoveState();
                    final int draggedPieceIndex = BoardRenderer.indexOfPosition(e.getX(), e.getY());
                    moveState.setDraggedPieceIndex(draggedPieceIndex);
                    moveState.setDraggedPiece(board.getPosition()[draggedPieceIndex]);
                    moveState.setLegalMoves(Moves.legalMoves(board.getPosition(), moveState.getDraggedPieceIndex(), moveState));
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    arrowStart = BoardRenderer.centreOfHoveredSquare(e.getX(), e.getY());
                }
            }

            @Override
            public void mouseReleased(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    final MoveState moveState = boardRenderer.getMoveState();
                    // check if square is legal and make the move or
                    // move back to origin
                    final int position = BoardRenderer.indexOfPosition(e.getX(), e.getY());
                    if (moveState.getLegalMoves().contains(position)) {
                        if (position != moveState.getDraggedPieceIndex()) {
                            final Move move = Moves.move(board.getPosition(), moveState.getDraggedPieceIndex(), BoardRenderer.indexOfPosition(e.getX(), e.getY()));
                            move.doMove(board.getPosition());
                            moveState.nextTurn();
                            moveState.pushMove(move);
                            if (autoMove) {
                                final Move response = ChessAI.move(board.getPosition(), moveState);
                                moveState.pushMove(response);
                            }
                            moveState.getRedoStack().clear();
                        }
                    }
                    moveState.setDraggedPieceIndex(-1);
                    moveState.setDraggedPiece(Piece.NONE);
                    moveState.setLegalMoves(new ArrayList<>());
                } else if (e.getButton() == MouseEvent.BUTTON3) {
                    if (arrowStart != null) {
                        final Vector2f arrowEnd = BoardRenderer.centreOfHoveredSquare(e.getX(), e.getY());
                        if (arrowStart.equals(arrowEnd)) {
                            // toggle mark instead
                            final int x = (int) (arrowStart.getX() - BoardRenderer.SQUARE_SIZE.getWidth() * .5f);
                            final int y = (int) (arrowStart.getY() - BoardRenderer.SQUARE_SIZE.getHeight() * .5f);
                            boardRenderer.getMoveState().toggleSquareMark(new SquareMark(new Vector2f(x, y)));
                        } else {
                            // toggle arrow
                            boardRenderer.getMoveState().toggleArrow(new Arrow(arrowStart, arrowEnd));
                        }
                        arrowStart = null;
                    }
                }
            }

            @Override
            public void mouseMoved(final MouseEvent e) {
                boardRenderer.getMoveState().setHoveredSquare(BoardRenderer.indexOfPosition(e.getX(), e.getY()));
            }
        });

        Input.addKeyboardInputHandler(new KeyboardInputAdapter() {
            private int buttonDown = -1;

            @Override
            public void keyPressed(final KeyEvent e) {
                buttonDown = e.getKeyCode();
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                // "key typed"
                if (buttonDown == e.getKeyCode()) {
                    if (buttonDown == KeyEvent.VK_LEFT) {
                        //undo move
                        final boolean undone = boardRenderer.getMoveState().undoMove(board.getPosition());
                        if (undone) {
                            boardRenderer.getMoveState().nextTurn();
                        }
                    } else if (buttonDown == KeyEvent.VK_RIGHT) {
                        //redo move
                        final boolean redone = boardRenderer.getMoveState().redoMove(board.getPosition());
                        if (redone) {
                            boardRenderer.getMoveState().nextTurn();
                        }
                    }
                    buttonDown = -1;
                }
            }

            @Override
            public void keyTyped(final KeyEvent e) {
                if (e.getKeyChar() == 'r') {
                    // reset position
                    board.setPosition(BoardPositions.parseFEN(Main.STARTING_FEN));
                    boardRenderer.getMoveState().clearMoveStacks();
                    boardRenderer.getMoveState().setColorToMove(Piece.LIGHT);
                    MoveState.allowedCastles.addAll(CastleMove.allCastles());
                } else if (e.getKeyChar() == 'm') {
                    // make the AI make the next move
                    final MoveState moveState = boardRenderer.getMoveState();
                    final Move response = ChessAI.move(board.getPosition(), moveState);
                    moveState.pushMove(response);
                    moveState.getRedoStack().clear();
                } else if (e.getKeyChar() == 'a') {
                    autoMove = !autoMove;
                    System.out.println("auto move " + (autoMove ? "on" : "off") + " - Chessica " + (autoMove ? "will" : "will not") +
                            " automatically move the enemy pieces.");
                }
            }
        });
    }
}
