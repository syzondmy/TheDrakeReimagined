package thedrake;

import java.io.PrintWriter;

public class Board implements JSONSerializable {

    private final BoardTile [][] boardTiles;
    private final int dimension;

    // Constructor. Creates square game board of given size (dimension = width = height), with all places empty (containing BoardTile.EMPTY)
    public Board(int dimension) {
        boardTiles = new BoardTile[dimension][dimension];
        this.dimension = dimension;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                boardTiles[i][j]= BoardTile.EMPTY;
            }
        }
    }

    // Size of the board
    public int dimension() {
        return dimension;
    }

    // Returns a tile on a provided position
    public BoardTile at(TilePos pos) {
        return boardTiles[pos.i()][pos.j()];
    }

        // Creates new board with new tiles provided by the ats parameter. All the other tiles stay the same
    public Board withTiles(TileAt... ats) {
        Board newBoard = new Board(dimension);

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                BoardPos bPos = new BoardPos(dimension,i,j);
                newBoard.boardTiles[i][j] = this.at(bPos);
            }
        }
        for (TileAt at : ats) {
            newBoard.boardTiles[at.pos.i()][at.pos.j()] = at.tile;
        }
        return  newBoard;
    }

    // Creates an instance of PositionFactory class for simpler creation of new position objects for this board
    public PositionFactory positionFactory() {
        return new PositionFactory(dimension);
    }

    public static class TileAt {
        public final BoardPos pos;
        public final BoardTile tile;

        public TileAt(BoardPos pos, BoardTile tile) {
            this.pos = pos;
            this.tile = tile;
        }
    }

    @Override
    public void toJSON(PrintWriter writer) {
        writer.print("{");

        //DIMENSION
        writer.print("\"dimension\":");
        writer.print(dimension);
        writer.print(",");

        //TILES
        writer.print("\"tiles\":[");
        boolean first = true;
        for (int j = 0; j < dimension; j++) { // row-major order
            for (int i = 0; i < dimension; i++) {
                if (!first) writer.print(",");
                first = false;
                boardTiles[i][j].toJSON(writer);
            }
        }
        writer.print("]");

        writer.print("}");
    }


}

