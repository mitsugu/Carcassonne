package carcassonne.model.tile;

import java.util.Collections;
import java.util.Stack;

/**
 * The stack of tiles for a game.
 * @author Timur Saglam
 */
public class TileStack {
    private final Stack<Tile> tiles;
    private final double multiplicator;

    /**
     * Simple constructor.
     * @param players is the amount of player for which this tile stack is intended.
     */
    public TileStack(int players) {
        multiplicator = 0.4 + players * 0.3;
        tiles = new Stack<>();
        fillStack();
    }

    /**
     * Draws random tile from the stack and returns it
     * @return the tile or null if the stack is empty.
     */
    public Tile drawTile() {
        if (tiles.isEmpty()) {
            return null;
        }
        return tiles.pop();
    }

    /**
     * Getter for the size of the stack.
     * @return the amount of tiled on the stack.
     */
    public int getSize() {
        return tiles.size();
    }

    /**
     * Checks whether the tile stack is empty.
     * @return true if empty.
     */
    public boolean isEmpty() {
        return tiles.isEmpty();
    }

    private void fillStack() {
        for (TileType tileType : TileType.values()) {
            double amount = Math.round(tileType.getAmount() * multiplicator);
            for (int i = 0; i < amount; i++) {
                tiles.add(TileFactory.create(tileType));
            }
        }
        Collections.shuffle(tiles);
    }
}
