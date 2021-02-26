package de.sanj0.jess;

import de.edgelord.saltyengine.core.graphics.SaltyGraphics;
import de.edgelord.saltyengine.graphics.image.SaltyImage;
import de.edgelord.saltyengine.graphics.sprite.Spritesheet;
import de.edgelord.saltyengine.transform.Transform;
import de.edgelord.saltyengine.utils.ImageLoader;
import de.edgelord.saltyengine.utils.ImageUtils;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static de.sanj0.jess.Piece.*;

/**
 * Renders pieces
 * pngs from Sebastian Lague on github, copyright notice in root/src/main/resources/img/copyright.txt
 */
public class PieceRenderer {
    private static final SaltyImage sheet = ImageLoader.getOrLoadImage("spritesheet", "img/pieces.png");
    
    private static SaltyImage PAWN_LIGHT;
    private static SaltyImage PAWN_DARK;
    private static SaltyImage KNIGHT_LIGHT;
    private static SaltyImage KNIGHT_DARK;
    private static SaltyImage BISHOP_LIGHT;
    private static SaltyImage BISHOP_DARK;
    private static SaltyImage ROOK_LIGHT;
    private static SaltyImage ROOK_DARK;
    private static SaltyImage QUEEN_LIGHT;
    private static SaltyImage QUEEN_DARK;
    private static SaltyImage KING_LIGHT;
    private static SaltyImage KING_DARK;
    
    private static Map<Byte, SaltyImage> imageMap = new HashMap<>();

    public static void drawPiece(final SaltyGraphics g, final byte piece, final Transform transform) {
        if (piece == NONE) {
            return;
        }
        g.drawImage(imageMap.get(piece), transform.getPosition());
    }
    
    public static void init() {
        final Spritesheet spritesheet = new Spritesheet(sheet, 333.33f, 334f);
        KING_LIGHT = getPieceImage(spritesheet, 0, 0);
        QUEEN_LIGHT = getPieceImage(spritesheet, 1, 0);
        BISHOP_LIGHT = getPieceImage(spritesheet, 2, 0);
        KNIGHT_LIGHT = getPieceImage(spritesheet, 3, 0);
        ROOK_LIGHT = getPieceImage(spritesheet, 4, 0);
        PAWN_LIGHT = getPieceImage(spritesheet, 5, 0);

        KING_DARK = getPieceImage(spritesheet, 0, 1);
        QUEEN_DARK = getPieceImage(spritesheet, 1, 1);
        BISHOP_DARK = getPieceImage(spritesheet, 2, 1);
        KNIGHT_DARK = getPieceImage(spritesheet, 3, 1);
        ROOK_DARK = getPieceImage(spritesheet, 4, 1);
        PAWN_DARK = getPieceImage(spritesheet, 5, 1);
        
        imageMap.put(get(PAWN, LIGHT), PAWN_LIGHT);
        imageMap.put(get(KNIGHT, LIGHT), KNIGHT_LIGHT);
        imageMap.put(get(BISHOP, LIGHT), BISHOP_LIGHT);
        imageMap.put(get(ROOK, LIGHT), ROOK_LIGHT);
        imageMap.put(get(QUEEN, LIGHT), QUEEN_LIGHT);
        imageMap.put(get(KING, LIGHT), KING_LIGHT);

        imageMap.put(get(PAWN, DARK), PAWN_DARK);
        imageMap.put(get(KNIGHT, DARK), KNIGHT_DARK);
        imageMap.put(get(BISHOP, DARK), BISHOP_DARK);
        imageMap.put(get(ROOK, DARK), ROOK_DARK);
        imageMap.put(get(QUEEN, DARK), QUEEN_DARK);
        imageMap.put(get(KING, DARK), KING_DARK);
    }

    private static SaltyImage getPieceImage(final Spritesheet spritesheet, final int x, final int y) {
        return ImageUtils.resize(spritesheet.getFrame(x, y).getImage(), 100, 100);
    }
}
