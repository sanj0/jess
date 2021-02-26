package de.sanj0.jess;

import de.edgelord.saltyengine.core.Game;
import de.edgelord.saltyengine.core.GameConfig;

public class Main extends Game {

    public static void main(String[] args) {
        init(GameConfig.config(800, 800, "Jess", 5));
        PieceRenderer.init();
        start(30, new ChessScene(new Board(BoardPositions.parseFEN(BoardPositions.STARTING_POSITION))));
    }
}
