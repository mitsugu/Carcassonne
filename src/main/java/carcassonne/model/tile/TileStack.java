package carcassonne.model.tile;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.Stack;

/**
 * The stack of tiles for a game.
 * @author Timur Saglam
 */
public class TileStack {
    private final Stack<Tile> tiles;
    private final Queue<Tile> returnedTiles;
    private final Set<Tile> returnHistory;
    private final int multiplicator;

    /**
     * Basic constructor, creates the tile stack.
     * @param distribution is the tile distribution according which the stack is filled.
     * @param multiplicator is the tile stack multiplier, meaning how often the distribution is added to the stack.
     */
    public TileStack(TileDistribution distribution, int multiplicator) {
        this(distribution, multiplicator, null);
    }

    /**
     * Creates a tile stack with a pseudo-random tile order.
     * @param distribution is the tile distribution according which the stack is filled.
     * @param multiplicator is the tile stack multiplier, meaning how often the distribution is added to the stack.
     * @param sortingSeed is the seed for the tile order.
     */
    public TileStack(TileDistribution distribution, int multiplicator, Long sortingSeed) {
        this.multiplicator = multiplicator;
        tiles = new Stack<>();
        returnedTiles = new LinkedList<>();
        returnHistory = new HashSet<>();
        fillStack(distribution);
        rotateRandomly();
        if (sortingSeed == null) {
            Collections.shuffle(tiles);
        } else {
            Collections.shuffle(tiles, new Random(sortingSeed));
        }
        while (getSize() > 2) {
            drawTile();
        }
    }

    /**
     * Draws random tile from the stack and returns it
     * @return the tile or null if the stack is empty.
     */
    public Tile drawTile() {
        if (tiles.isEmpty() && returnedTiles.isEmpty()) {
            return null; // no tile to draw!
        } else if (tiles.isEmpty()) {
            return returnedTiles.poll();
        }
        return tiles.pop();
    }

    /**
     * Returns a tile that is not placed under the stack.
     * @param tile is the tile to put back under the stack.
     */
    public void putBack(Tile tile) {
        if (tile.isPlaced()) {
            throw new IllegalArgumentException("Cannot return a placed tile!");
        }
        if (returnHistory.add(tile)) { // tiles can only be returned once!
            returnedTiles.add(tile);
        }
    }

    /**
     * Getter for the size of the stack.
     * @return the amount of tiled on the stack.
     */
    public int getSize() {
        return tiles.size() + returnedTiles.size();
    }

    /**
     * Checks whether the tile stack is empty.
     * @return true if empty.
     */
    public boolean isEmpty() {
        return tiles.isEmpty() && returnedTiles.isEmpty();
    }

    private void fillStack(TileDistribution distribution) {
        for (TileType tileType : TileType.validTiles()) {
            int amount = distribution.getQuantity(tileType) * multiplicator;
            for (int i = 0; i < amount; i++) {
                tiles.add(new Tile(tileType));
            }
        }
    }

    private void rotateRandomly() {
        for (Tile tile : tiles) {
            for (int i = 0; i < Math.round(Math.random() * 4 - 0.5); i++) {
                tile.rotateRight(); // Random orientation with equal chance for each orientation.
            }
        }
    }
}
