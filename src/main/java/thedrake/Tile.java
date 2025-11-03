package thedrake;

import java.util.List;

public interface Tile {

    // Returns true if the tile is empty (can be stepped on or placed on)
    public boolean canStepOn();

    // Returns true if the tile has a troop on it
    public boolean hasTroop();

    public List<Move> movesFrom(BoardPos pos, GameState state);
}
