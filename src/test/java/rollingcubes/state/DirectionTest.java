package rollingcubes.state;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DirectionTest {

    @Test
    void testOf() {
        assertEquals(Direction.UP, Direction.of(-1, 0));
        assertEquals(Direction.RIGHT, Direction.of(0, 1));
        assertEquals(Direction.DOWN, Direction.of(1, 0));
        assertEquals(Direction.LEFT, Direction.of(0, -1));
    }

    @Test
    void testOf_shouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> Direction.of(1, 1));
    }

}
