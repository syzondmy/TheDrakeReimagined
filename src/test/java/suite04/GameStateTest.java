package suite04;

import org.junit.Test;
import thedrake.*;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class GameStateTest {

    private GameState createTestState() {
        Board board = new Board(3);
        PositionFactory pf = board.positionFactory();
        board = board.withTiles(new Board.TileAt(pf.pos("a3"), BoardTile.MOUNTAIN));
        return new StandardDrakeSetup().startState(board);
    }

    private GameState createTestStateWithoutMountain() {
        Board board = new Board(3);
        return new StandardDrakeSetup().startState(board);
    }

    @Test
    public void classStructure() {
        // All attributes private and final
        for (Field f : GameState.class.getFields()) {
            assertTrue(Modifier.isPrivate(f.getModifiers()));
            assertTrue(Modifier.isFinal(f.getModifiers()));
        }
    }

    @Test
    public void introGame() {
        GameState state = createTestState();
        PositionFactory pf = state.board().positionFactory();

        assertFalse(state.canPlaceFromStack(TilePos.OFF_BOARD));

        // Placing the blue leader
        assertTrue(state.canPlaceFromStack(pf.pos("a1")));
        assertTrue(state.canPlaceFromStack(pf.pos("b1")));
        assertTrue(state.canPlaceFromStack(pf.pos("c1")));
        assertFalse(state.canPlaceFromStack(pf.pos("a2")));
        assertFalse(state.canPlaceFromStack(pf.pos("b2")));
        assertFalse(state.canPlaceFromStack(pf.pos("c2")));
        assertFalse(state.canPlaceFromStack(pf.pos("a3")));
        assertFalse(state.canPlaceFromStack(pf.pos("b3")));
        assertFalse(state.canPlaceFromStack(pf.pos("c3")));

        state = state.placeFromStack(pf.pos("a1"));

        // Placing the orange leader
        assertFalse(state.canPlaceFromStack(pf.pos("a1")));
        assertFalse(state.canPlaceFromStack(pf.pos("b1")));
        assertFalse(state.canPlaceFromStack(pf.pos("c1")));
        assertFalse(state.canPlaceFromStack(pf.pos("a2")));
        assertFalse(state.canPlaceFromStack(pf.pos("b2")));
        assertFalse(state.canPlaceFromStack(pf.pos("c2")));
        assertFalse(state.canPlaceFromStack(pf.pos("a3")));
        assertTrue(state.canPlaceFromStack(pf.pos("b3")));
        assertTrue(state.canPlaceFromStack(pf.pos("c3")));

        state = state.placeFromStack(pf.pos("c3"));

        // Placing first blue guard
        assertFalse(state.canPlaceFromStack(pf.pos("a1")));
        assertTrue(state.canPlaceFromStack(pf.pos("b1")));
        assertFalse(state.canPlaceFromStack(pf.pos("c1")));
        assertTrue(state.canPlaceFromStack(pf.pos("a2")));
        assertFalse(state.canPlaceFromStack(pf.pos("b2")));
        assertFalse(state.canPlaceFromStack(pf.pos("c2")));
        assertFalse(state.canPlaceFromStack(pf.pos("a3")));
        assertFalse(state.canPlaceFromStack(pf.pos("b3")));
        assertFalse(state.canPlaceFromStack(pf.pos("c3")));

        // No steps or capturing before guards are placed
        assertFalse(state.canStep(pf.pos("a1"), pf.pos("a2")));
        assertFalse(state.canCapture(pf.pos("a1"), pf.pos("c3")));

        state = state.placeFromStack(pf.pos("a2"));

        // Placing first orange guard
        assertFalse(state.canPlaceFromStack(pf.pos("a1")));
        assertFalse(state.canPlaceFromStack(pf.pos("b1")));
        assertFalse(state.canPlaceFromStack(pf.pos("c1")));
        assertFalse(state.canPlaceFromStack(pf.pos("a2")));
        assertFalse(state.canPlaceFromStack(pf.pos("b2")));
        assertTrue(state.canPlaceFromStack(pf.pos("c2")));
        assertFalse(state.canPlaceFromStack(pf.pos("a3")));
        assertTrue(state.canPlaceFromStack(pf.pos("b3")));
        assertFalse(state.canPlaceFromStack(pf.pos("c3")));

        // No steps or capturing before guards are placed
        assertFalse(state.canStep(pf.pos("c3"), pf.pos("c2")));
        assertFalse(state.canCapture(pf.pos("c3"), pf.pos("a1")));

        state = state.placeFromStack(pf.pos("b3"));

        // Placing second blue guard
        assertFalse(state.canPlaceFromStack(pf.pos("a1")));
        assertTrue(state.canPlaceFromStack(pf.pos("b1")));
        assertFalse(state.canPlaceFromStack(pf.pos("c1")));
        assertFalse(state.canPlaceFromStack(pf.pos("a2")));
        assertFalse(state.canPlaceFromStack(pf.pos("b2")));
        assertFalse(state.canPlaceFromStack(pf.pos("c2")));
        assertFalse(state.canPlaceFromStack(pf.pos("a3")));
        assertFalse(state.canPlaceFromStack(pf.pos("b3")));
        assertFalse(state.canPlaceFromStack(pf.pos("c3")));

        state = state.placeFromStack(pf.pos("b1"));

        // Placing second orange guard
        assertFalse(state.canPlaceFromStack(pf.pos("a1")));
        assertFalse(state.canPlaceFromStack(pf.pos("b1")));
        assertFalse(state.canPlaceFromStack(pf.pos("c1")));
        assertFalse(state.canPlaceFromStack(pf.pos("a2")));
        assertFalse(state.canPlaceFromStack(pf.pos("b2")));
        assertTrue(state.canPlaceFromStack(pf.pos("c2")));
        assertFalse(state.canPlaceFromStack(pf.pos("a3")));
        assertFalse(state.canPlaceFromStack(pf.pos("b3")));
        assertFalse(state.canPlaceFromStack(pf.pos("c3")));

        state = state.placeFromStack(pf.pos("c2"));

        Set<BoardPos> allBoardPositions = Stream.of("a1", "a2", "a3", "b1", "b2", "b3", "c1", "c2", "c3").map(pf::pos).collect(Collectors.toSet());
        List<BoardPos> initialTroopPositions = Stream.of("a1", "c3", "a2", "b3", "b1", "c2").map(pf::pos).toList();

        // Check tile contents after intial troop placement
        checkTilesForTroops(state, allBoardPositions, initialTroopPositions);
    }

    @Test
    public void middleGameBlue() {
        GameState state = createTestState();
        PositionFactory pf = state.board().positionFactory();
        Set<BoardPos> allBoardPositions = Stream.of("a1", "a2", "a3", "b1", "b2", "b3", "c1", "c2", "c3").map(pf::pos).collect(Collectors.toSet());
        List<BoardPos> initialTroopPositions = Stream.of("a1", "c3", "a2", "b3", "b1", "c2").map(pf::pos).toList();

        // Intial troop placement
        for (BoardPos eachTroopPosition : initialTroopPositions)
            state = state.placeFromStack(eachTroopPosition);

        // Check tile contents after intial troop placement
        checkTilesForTroops(state, allBoardPositions, initialTroopPositions);

        // Placing blue troop
        assertFalse(state.canPlaceFromStack(pf.pos("a1")));
        assertFalse(state.canPlaceFromStack(pf.pos("b1")));
        assertTrue(state.canPlaceFromStack(pf.pos("c1")));
        assertFalse(state.canPlaceFromStack(pf.pos("a2")));
        assertTrue(state.canPlaceFromStack(pf.pos("b2")));
        assertFalse(state.canPlaceFromStack(pf.pos("c2")));
        assertFalse(state.canPlaceFromStack(pf.pos("a3")));
        assertFalse(state.canPlaceFromStack(pf.pos("b3")));
        assertFalse(state.canPlaceFromStack(pf.pos("c3")));

        // Stepping with blue troop
        assertFalse(state.canStep(pf.pos("a1"), pf.pos("a3")));
        assertTrue(state.canStep(pf.pos("a1"), pf.pos("b2")));
        assertTrue(state.canStep(pf.pos("a1"), pf.pos("c1")));
        assertFalse(state.canStep(pf.pos("a1"), pf.pos("a1")));
        assertFalse(state.canStep(pf.pos("a1"), pf.pos("a2")));
        assertFalse(state.canStep(pf.pos("a1"), pf.pos("b1")));
        assertFalse(state.canStep(pf.pos("a1"), pf.pos("b3")));
        assertFalse(state.canStep(pf.pos("a1"), pf.pos("c2")));
        assertFalse(state.canStep(pf.pos("a1"), pf.pos("c3")));

        // Capturing with blue troop
        assertFalse(state.canCapture(pf.pos("a1"), pf.pos("a3")));
        assertFalse(state.canCapture(pf.pos("a1"), pf.pos("b2")));
        assertFalse(state.canCapture(pf.pos("a1"), pf.pos("c1")));
        assertFalse(state.canCapture(pf.pos("a1"), pf.pos("a1")));
        assertFalse(state.canCapture(pf.pos("a1"), pf.pos("a2")));
        assertFalse(state.canCapture(pf.pos("a1"), pf.pos("b1")));
        assertTrue(state.canCapture(pf.pos("a1"), pf.pos("b3")));
        assertTrue(state.canCapture(pf.pos("a1"), pf.pos("c2")));
        assertTrue(state.canCapture(pf.pos("a1"), pf.pos("c3")));

        // Boundaries
        assertFalse(state.canStep(TilePos.OFF_BOARD, pf.pos("b2")));
        assertFalse(state.canStep(pf.pos("a1"), TilePos.OFF_BOARD));

        assertFalse(state.canCapture(TilePos.OFF_BOARD, pf.pos("c3")));
        assertFalse(state.canCapture(pf.pos("a1"), TilePos.OFF_BOARD));
    }

    @Test
    public void middleGameBlueWithoutMountain() {
        GameState state = createTestStateWithoutMountain();
        PositionFactory pf = state.board().positionFactory();
        Set<BoardPos> allBoardPositions = Stream.of("a1", "a2", "a3", "b1", "b2", "b3", "c1", "c2", "c3").map(pf::pos).collect(Collectors.toSet());
        List<BoardPos> initialTroopPositions = Stream.of("b1", "c3", "a1", "b3", "c1", "c2").map(pf::pos).toList();

        // Intial troop placement
        for (BoardPos eachTroopPosition : initialTroopPositions)
            state = state.placeFromStack(eachTroopPosition);

        // Check tile contents after intial troop placement
        checkTilesForTroops(state, allBoardPositions, initialTroopPositions);

        // Placing blue troop
        assertFalse(state.canPlaceFromStack(pf.pos("a1")));
        assertFalse(state.canPlaceFromStack(pf.pos("b1")));
        assertFalse(state.canPlaceFromStack(pf.pos("c1")));
        assertTrue(state.canPlaceFromStack(pf.pos("a2")));
        assertTrue(state.canPlaceFromStack(pf.pos("b2")));
        assertFalse(state.canPlaceFromStack(pf.pos("c2")));
        assertFalse(state.canPlaceFromStack(pf.pos("a3"))); //not next to already placed unit
        assertFalse(state.canPlaceFromStack(pf.pos("b3")));
        assertFalse(state.canPlaceFromStack(pf.pos("c3")));
    }

    @Test
    public void middleGameOrange() {
        GameState state = createTestState();
        PositionFactory pf = state.board().positionFactory();
        Set<BoardPos> allBoardPositions = Stream.of("a1", "a2", "a3", "b1", "b2", "b3", "c1", "c2", "c3").map(pf::pos).collect(Collectors.toSet());
        List<BoardPos> initialTroopPositions = Stream.of("a1", "c3", "a2", "b3", "b1", "c2", "b2").map(pf::pos).toList();

        // Intial troop placement
        for (BoardPos eachTroopPosition : initialTroopPositions)
            state = state.placeFromStack(eachTroopPosition);

        // Check tile contents after intial troop placement
        checkTilesForTroops(state, allBoardPositions, initialTroopPositions);

        // Placing orange troop
        assertFalse(state.canPlaceFromStack(pf.pos("a1")));
        assertFalse(state.canPlaceFromStack(pf.pos("b1")));
        assertTrue(state.canPlaceFromStack(pf.pos("c1")));
        assertFalse(state.canPlaceFromStack(pf.pos("a2")));
        assertFalse(state.canPlaceFromStack(pf.pos("b2")));
        assertFalse(state.canPlaceFromStack(pf.pos("c2")));
        assertFalse(state.canPlaceFromStack(pf.pos("a3")));
        assertFalse(state.canPlaceFromStack(pf.pos("b3")));
        assertFalse(state.canPlaceFromStack(pf.pos("c3")));

        // Stepping with orange troop
        assertFalse(state.canStep(pf.pos("c3"), pf.pos("a3")));
        assertFalse(state.canStep(pf.pos("c3"), pf.pos("b2")));
        assertTrue(state.canStep(pf.pos("c3"), pf.pos("c1")));
        assertFalse(state.canStep(pf.pos("c3"), pf.pos("a1")));
        assertFalse(state.canStep(pf.pos("c3"), pf.pos("a2")));
        assertFalse(state.canStep(pf.pos("c3"), pf.pos("b1")));
        assertFalse(state.canStep(pf.pos("c3"), pf.pos("b3")));
        assertFalse(state.canStep(pf.pos("c3"), pf.pos("c2")));
        assertFalse(state.canStep(pf.pos("c3"), pf.pos("c3")));

        // Capturing with orange troop
        assertFalse(state.canCapture(pf.pos("c3"), pf.pos("a3")));
        assertTrue(state.canCapture(pf.pos("c3"), pf.pos("b2")));
        assertFalse(state.canCapture(pf.pos("c3"), pf.pos("c1")));
        assertTrue(state.canCapture(pf.pos("c3"), pf.pos("a1")));
        assertTrue(state.canCapture(pf.pos("c3"), pf.pos("a2")));
        assertTrue(state.canCapture(pf.pos("c3"), pf.pos("b1")));
        assertFalse(state.canCapture(pf.pos("c3"), pf.pos("b3")));
        assertFalse(state.canCapture(pf.pos("c3"), pf.pos("c2")));
        assertFalse(state.canCapture(pf.pos("c3"), pf.pos("c3")));
    }

    @Test
    public void emptyStack() {
        Board board = new Board(3);
        PositionFactory pf = board.positionFactory();

        GameState state = new GameState(
                board,
                new Army(PlayingSide.BLUE, Collections.emptyList()),
                new Army(PlayingSide.ORANGE, Collections.emptyList())
        );

        // No placing from an empty stack
        assertFalse(state.canPlaceFromStack(pf.pos("a1")));
        assertFalse(state.canPlaceFromStack(pf.pos("a1")));
    }

    private void checkTilesForTroops(GameState state, Collection<BoardPos> allBoardPositions, Collection<BoardPos> initialTroopPositions) {
        for (BoardPos eachTroopPosition : allBoardPositions) {
            if (initialTroopPositions.contains(eachTroopPosition))
                assertTrue(
                        "Tile at " + eachTroopPosition.toString() + " should have a troop but tileAt returned a tile without a troop",
                        state.tileAt(eachTroopPosition).hasTroop()
                );
            else
                assertFalse(
                        "Tile at " + eachTroopPosition.toString() + " should not have a troop but tileAt returned a tile with a troop",
                        state.tileAt(eachTroopPosition).hasTroop()
                );
        }
    }
}
