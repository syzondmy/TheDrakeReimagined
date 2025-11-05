package suite01;

import org.junit.Test;
import thedrake.Offset2D;

import java.lang.reflect.Modifier;

import static org.junit.Assert.*;

public class Offset2DTest {

    @Test
    public void classStructure() {
        // The class contains coordinates as two public immutables
        // Both x and y are ints
        try {
            assertTrue(Modifier.isFinal(
                    Offset2D.class.getField("x").getModifiers()));
            assertTrue(Modifier.isFinal(
                    Offset2D.class.getField("y").getModifiers()));

            assertSame(Offset2D.class.getField("x").getType(), int.class);
            assertSame(Offset2D.class.getField("y").getType(), int.class);
        } catch (NoSuchFieldException e) {
            fail();
        }
    }

    @Test
    public void behaviour() {
        // Constructor that accepts two int parameters
        Offset2D offset2D = new Offset2D(10, -5);

        assertSame(10, offset2D.x);
        assertSame(-5, offset2D.y);

        // Method for comparison with other coordinates
        assertTrue(offset2D.equalsTo(10, -5));
        assertFalse(offset2D.equalsTo(9, -5));
        assertFalse(offset2D.equalsTo(10, -4));
        assertFalse(offset2D.equalsTo(0, 1));

        // Creates new Offset2D object with negated y coordinate
        assertTrue(offset2D.yFlipped().equalsTo(10, 5));
        assertTrue(offset2D.yFlipped().yFlipped().equalsTo(10, -5));
        assertNotSame(offset2D.yFlipped(), offset2D);
    }
}
