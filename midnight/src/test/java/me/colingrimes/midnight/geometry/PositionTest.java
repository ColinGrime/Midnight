package me.colingrimes.midnight.geometry;

import org.bukkit.World;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PositionTest {

    private @Mock World world;
    private final Position position = Position.of(world, 1, 1, 1);

    @Test
    void testYawRotation() {
        Position expected = Position.of(world, -1, 1, 1);
        Position actual = position.rotate(Rotation.of(90, 0, 0));
        assertPositionEquals(expected, actual);
    }

    @Test
    void testPitchRotation() {
        Position expected = Position.of(world, 1, 1, -1);
        Position actual = position.rotate(Rotation.of(0, 90, 0));
        assertPositionEquals(expected, actual);
    }

    @Test
    void testRollRotation() {
        Position expected = Position.of(world, 1, -1, 1);
        Position actual = position.rotate(Rotation.of(0, 0, 90));
        assertPositionEquals(expected, actual);
    }

    @Test
    void testMultipleRotations() {
        Position expected = Position.of(world, -1, 1, 1);
        Position actual = position.rotate(Rotation.of(90, 90, 90));
        assertPositionEquals(expected, actual);
    }

    /**
     * Assets that the two positions are equal.
     *
     * @param expected the expected position
     * @param actual the actual position
     */
    void assertPositionEquals(@Nonnull Position expected, @Nonnull Position actual) {
        assertEquals(expected.getX(), actual.getX(), 1E-9);
        assertEquals(expected.getY(), actual.getY(), 1E-9);
        assertEquals(expected.getZ(), actual.getZ(), 1E-9);
    }
}
