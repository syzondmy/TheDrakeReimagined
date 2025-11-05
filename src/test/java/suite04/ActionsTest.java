package suite04;

import org.junit.Test;
import thedrake.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class ActionsTest {

    private Set<Move> makeSet(Move... moves) {
        return new HashSet<>(Arrays.asList(moves));
    }

    @Test
    public void test() {
        Board board = new Board(4);
        PositionFactory pf = board.positionFactory();
        board = board.withTiles(new Board.TileAt(pf.pos("c1"),BoardTile.MOUNTAIN));
        StandardDrakeSetup setup = new StandardDrakeSetup();

        BoardTroops blueTroops = new BoardTroops(PlayingSide.BLUE);
        blueTroops = blueTroops
                .placeTroop(setup.DRAKE, pf.pos("b1"))
                .placeTroop(setup.CLUBMAN, pf.pos("a1"))
                .placeTroop(setup.SPEARMAN, pf.pos("b2"));
        Army blueArmy = new Army(blueTroops, Collections.emptyList(), Collections.emptyList());

        BoardTroops orangeTroops = new BoardTroops(PlayingSide.ORANGE);
        orangeTroops = orangeTroops
                .placeTroop(setup.DRAKE, pf.pos("c4"))
                .placeTroop(setup.MONK, pf.pos("c3"))
                .placeTroop(setup.CLUBMAN, pf.pos("b3"));
        Army orangeArmy = new Army(orangeTroops, Collections.emptyList(), Collections.emptyList());

        GameState state = new GameState(board, blueArmy, orangeArmy);

        // Slide: Drake at b1 cannot move anywhere, as own units are on a1 and b2 and mountain is on c1
        assertEquals(
                makeSet(),
                new HashSet<Move>(
                        state.tileAt(pf.pos("b1")).movesFrom(pf.pos("b1"), state)
                )
        );

        board = board.withTiles(new Board.TileAt(pf.pos("c1"),BoardTile.EMPTY));
        state = new GameState(board, blueArmy, orangeArmy);

        // Now the same slide without the mountain - Drake can slide in the c1 direction:
        assertEquals(
                makeSet(
                        new StepOnly(pf.pos("b1"), pf.pos("c1")),
                        new StepOnly(pf.pos("b1"), pf.pos("d1"))
                ),
                new HashSet<Move>(
                        state.tileAt(pf.pos("b1")).movesFrom(pf.pos("b1"), state)
                )
        );

        // Blue spearman can capture orange clubman on b3, then move there, or can capture the orange drake.
        // There is nothing to capture on a4.
        assertEquals(
                makeSet(
                        new StepAndCapture(pf.pos("b2"), pf.pos("b3")),
                        new CaptureOnly(pf.pos("b2"), pf.pos("c4"))
                ),
                new HashSet<Move>(
                        state.tileAt(pf.pos("b2")).movesFrom(pf.pos("b2"), state)
                )
        );

        // We ignore the possible list of moves and force the drake to move over the mountain for testing purposes
        state = state.stepOnly(pf.pos("b1"), pf.pos("d1"));

        // Slide: drake at c4 can move to free spaces in both directions
        assertEquals(
                makeSet(
                        new StepOnly(pf.pos("c4"), pf.pos("a4")),
                        new StepOnly(pf.pos("c4"), pf.pos("b4")),
                        new StepOnly(pf.pos("c4"), pf.pos("d4"))
                ),
                new HashSet<Move>(
                        state.tileAt(pf.pos("c4")).movesFrom(pf.pos("c4"), state)
                )
        );

        // Slide: monk at c3 can capture enemy unit, but cannot pass through it
        assertEquals(
                makeSet(
                        new StepAndCapture(pf.pos("c3"), pf.pos("b2")),
                        new StepOnly(pf.pos("c3"), pf.pos("d2")),
                        new StepOnly(pf.pos("c3"), pf.pos("b4")),
                        new StepOnly(pf.pos("c3"), pf.pos("d4"))
                ),
                new HashSet<Move>(
                        state.tileAt(pf.pos("c3")).movesFrom(pf.pos("c3"), state)
                )
        );

        assertEquals(
                makeSet(
                        new StepOnly(pf.pos("b3"), pf.pos("a3")),
                        new StepOnly(pf.pos("b3"), pf.pos("b4")),
                        new StepAndCapture(pf.pos("b3"), pf.pos("b2"))
                ),
                new HashSet<Move>(
                        state.tileAt(pf.pos("b3")).movesFrom(pf.pos("b3"), state)
                )
        );

    }
}
