package de.sanj0.jess;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.GameConfig;

public class Main extends Game {

    /**
     * The FEN notation of the position,
     * the sandbox started with.
     * <br>This is either {@link BoardPositions#STARTING_POSITION}
     * or a command line argument.
     */
    public static String STARTING_FEN = BoardPositions.STARTING_POSITION;

    public static void main(String[] args) {
        if (args.length == 1) {
            STARTING_FEN = args[0];
        }

        init(GameConfig.config(800, 800, "Jess", 5));
        PieceRenderer.init();
        start(30, new ChessScene(new Board(BoardPositions.parseFEN(STARTING_FEN))));
        Game.setDrawFPS(false);
        Game.getHostAsDisplayManager().getDisplay().setResizable(false);
    }
}
