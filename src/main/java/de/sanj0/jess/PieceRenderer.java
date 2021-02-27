package de.sanj0.jess;

import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.graphics.image.SaltyImage;
import de.edgelord.saltyengine.graphics.sprite.Spritesheet;
import de.edgelord.saltyengine.transform.Transform;
import de.edgelord.saltyengine.utils.ImageLoader;
import de.edgelord.saltyengine.utils.ImageUtils;

import java.util.HashMap;
import java.util.Map;

import static de.sanj0.jess.Piece.*;

/**
 * Renders chess pieces.
 * <p>
 * The png located at /src/main/resources/img/pieces.png is from Sebastian Lague
 * on github, copyright notice in /src/main/resources/img/pieces.copyright.txt
 */
public class PieceRenderer {
    /**
     * The spritesheet image that contains all the pieces.
     */
    private static final SaltyImage sheet = ImageLoader.getOrLoadImage("spritesheet", "img/pieces.png");
    /**
     * How many rows of sprites are there in the spritesheet?
     */
    private static final int SPRITE_ROWS = 2;
    /**
     * How many sprites PER row are there in the spritesheet?
     */
    private static final int SPRITES_PER_ROW = 6;
    /**
     * The order of pieces in the spritesheet, from left to right and top to
     * bottom.
     */
    private static final byte[] SPRITES_KEY = {
            LIGHT | KING,
            LIGHT | QUEEN,
            LIGHT | BISHOP,
            LIGHT | KNIGHT,
            LIGHT | ROOK,
            LIGHT | PAWN,
            // next row
            DARK | KING,
            DARK | QUEEN,
            DARK | BISHOP,
            DARK | KNIGHT,
            DARK | ROOK,
            DARK | PAWN
    };
    /**
     * The map used to store and later query the piece images
     */
    private static final Map<Byte, SaltyImage> imageMap = new HashMap<>();

    /**
     * Empty constructor to avoid the generation of the default public one.
     */
    private PieceRenderer() {
    }

    /**
     * Draws the given piece to the given {@link SaltyGraphics graphics} by
     * querying an image from the {@link #imageMap map} and drawing it at the
     * position of the given {@link Transform}, ignoring its size and instead
     * using the default size of the queried image, which is equal to {@link
     * BoardRenderer#squareSize}.
     *
     * @param g         the graphics to render to
     * @param piece     the piece to render
     * @param transform the position of the piece to be rendered at
     */
    public static void drawPiece(final SaltyGraphics g, final byte piece, final Transform transform) {
        if (piece == NONE) {
            return;
        }
        g.drawImage(imageMap.get(piece), transform.getPosition());
    }

    /**
     * Initializes the PieceRenderer by filling the {@link #imageMap image map}
     * with all the pieces from the {@link #sheet sprite sheet}.
     */
    public static void init() {
        final Spritesheet spritesheet = new Spritesheet(sheet, 333.33f, 334f);
        final float width = BoardRenderer.squareSize.getWidth();
        final float height = BoardRenderer.squareSize.getHeight();

        for (int y = 0; y < SPRITE_ROWS; y++) {
            for (int x = 0; x < SPRITES_PER_ROW; x++) {
                imageMap.put(SPRITES_KEY[y * SPRITES_PER_ROW + x],
                        getPieceImage(spritesheet, x, y, width, height));
            }
        }
    }

    /**
     * Gets the sprite specified by its x and y coordinates from the given
     * {@link Spritesheet}, scales it to the given width and height and returns
     * it.
     *
     * @param spritesheet the spritesheet
     * @param x           the x coordinate of the desired sprite
     * @param y           the y coordinate of the desired sprite
     * @param width       the width for the image to be scaled to
     * @param height      the height for the image to be scaled to
     *
     * @return the sprite at the given coordinates in the given SpriteSheet
     * sclaled to the given width and height
     * @see ImageUtils#resize(SaltyImage, float, float)
     * @see Spritesheet#getFrame(int, int)
     */
    private static SaltyImage getPieceImage(final Spritesheet spritesheet,
                                            final int x, final int y,
                                            final float width, final float height) {
        return ImageUtils.resize(spritesheet.getFrame(x, y).getImage(), width, height);
    }
}
