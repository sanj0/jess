package de.sanj0.jess;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.gameobject.DrawingRoutine;
import de.edgelord.saltyengine.graphics.image.SaltyBufferedImage;
import de.edgelord.saltyengine.graphics.image.SaltyImage;
import de.edgelord.saltyengine.input.Input;
import de.edgelord.saltyengine.transform.Dimensions;
import de.edgelord.saltyengine.transform.Transform;
import de.edgelord.saltyengine.transform.Vector2f;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Renders the board
 */
public class BoardRenderer extends DrawingRoutine {

    public static final Color LIGHT_COLOR = new Color(244, 245, 215);
    public static final Color DARK_COLOR = new Color(83, 103, 47);
    public static final Color HOVER_COLOR = new Color(255, 103, 47, 127);
    public static final Color LEGAL_MOVE_COLOR = new Color(255, 109, 172, 127);
    public static final Vector2f origin = Vector2f.zero();
    public static final Dimensions SQUARE_SIZE = new Dimensions(100, 100);
    private final Board board;
    private MoveState moveState =
            new MoveState(-1, -1, new ArrayList<>(), Piece.NONE);
    private final SaltyImage boardImage;

    public BoardRenderer(final Board board) {
        super(DrawingPosition.BEFORE_GAMEOBJECTS);
        this.board = board;

        System.out.println("board: ");
        System.out.println(Arrays.toString(board.getPosition()));

        boardImage = new SaltyBufferedImage((int) Game.getGameWidth(), (int) Game.getGameHeight());
        final SaltyGraphics g = boardImage.getGraphics();
        float x = origin.getX();
        float y = origin.getY();
        float width = SQUARE_SIZE.getWidth();
        float height = SQUARE_SIZE.getHeight();
        boolean isLight = true;
        for (int i = 0; i < 64; i++) {
            g.setColor(isLight ? LIGHT_COLOR : DARK_COLOR);
            g.drawRect(x, y, width, height);

            if ((i + 1) % 8 == 0) {
                x = origin.getX();
                y += height;
            } else {
                x += width;
                isLight = !isLight;
            }
        }
    }

    public static int indexOfPosition(final float x, final float y) {
        final int rank = (int) Math.floor(x / SQUARE_SIZE.getWidth());
        final int file = (int) Math.floor(y / SQUARE_SIZE.getHeight());
        // weird that it only works seemingly the wrong way...?
        return file * 8 + rank;
    }

    public static Vector2f centreOfHoveredSquare(final float x, final float y) {
        float cx;
        float cy;

        cx = (float) Math.floor(x / SQUARE_SIZE.getWidth()) * SQUARE_SIZE.getWidth() + SQUARE_SIZE.getWidth() / 2f;
        cy = (float) Math.floor(y / SQUARE_SIZE.getHeight()) * SQUARE_SIZE.getHeight() + SQUARE_SIZE.getHeight() / 2f;
        return new Vector2f(cx, cy);
    }

    @Override
    public void draw(final SaltyGraphics g) {
        g.drawImage(boardImage, 0, 0);

        for (int i = 0; i < moveState.getSquareMarks().size(); i++) {
            moveState.getSquareMarks().get(i).draw(g.copy());
        }

        /*
        g.setColor(LEGAL_MOVE_COLOR);
        for (final int legalMove : ChessScene.legalMoves) {
            final int file = legalMove / 8;
            final int rank = legalMove - file * 8;
            g.drawRect(origin.getX() + rank * squareSize.getWidth(),
                    origin.getY() + file * squareSize.getHeight(),
                    squareSize.getWidth(), squareSize.getHeight());
        }*/

        final byte[] position = board.getPosition();
        float x = origin.getX();
        float y = origin.getY();
        float width = SQUARE_SIZE.getWidth();
        float height = SQUARE_SIZE.getHeight();
        for (int i = 0; i < position.length; i++) {
            final byte piece = position[i];

            // highlight the hovered square
            if (moveState.getHoveredSquare() == i) {
                g.setColor(HOVER_COLOR);
                g.drawRect(x, y, width, height);
            }

            // draw piece
            if (moveState.getDraggedPieceIndex() != i) {
                PieceRenderer.drawPiece(g, piece, new Transform(x, y, width, height));
            }

            // update x and y
            if ((i + 1) % 8 == 0) {
                x = origin.getX();
                y += height;
            } else {
                x += width;
            }
        }

        for (int i = 0; i < moveState.getArrows().size(); i++) {
            moveState.getArrows().get(i).draw(g.copy());
        }
        final Vector2f cursor = Input.getCursorPosition();
        final float pieceX = cursor.getX() - width * .5f;
        final float pieceY = cursor.getY() - width * .5f;
        PieceRenderer.drawPiece(g, moveState.getDraggedPiece(), new Transform(pieceX, pieceY, width, height));
    }

    /**
     * Gets {@link #moveState}.
     *
     * @return the value of {@link #moveState}
     */
    public MoveState getMoveState() {
        return moveState;
    }

    /**
     * Sets {@link #moveState}.
     *
     * @param moveState the new value of {@link #moveState}
     */
    public void setMoveState(final MoveState moveState) {
        this.moveState = moveState;
    }
}
