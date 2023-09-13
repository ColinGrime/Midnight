package me.colingrimes.particles.particle;

import me.colingrimes.midnight.geometry.Point;
import me.colingrimes.midnight.geometry.Position;
import me.colingrimes.midnight.geometry.Rotation;
import me.colingrimes.particles.particle.implementation.BaseParticleEffect;
import me.colingrimes.particles.particle.util.ParticleEffectType;
import me.colingrimes.particles.particle.util.ParticleProperties;
import me.colingrimes.particles.particle.util.ParticleProperty;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import javax.annotation.Nonnull;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ParticleEffectTest {

    private Point<Rotation> mockPoint;
    private ParticleProperties properties;
    private ParticleEffect particleEffect;

    @BeforeEach
    public void setUp() {
        @SuppressWarnings("unchecked")
        Point<Rotation> mockPoint = mock(Point.class);
        this.mockPoint = mockPoint;

        Position pointPosition = mock(Position.class);
        Location pointLocation = mock(Location.class);

        when(mockPoint.getPosition()).thenReturn(pointPosition);
        when(pointPosition.toLocation()).thenReturn(pointLocation);

        properties = ParticleProperties.create();
        particleEffect = new BaseParticleEffect(mockPoint, properties) {
            @Override
            protected void spawnParticle() {
                // NO-OP
            }

            @Nonnull
            @Override
            public ParticleEffectType getType() {
                return ParticleEffectType.CIRCLE;
            }
        };

        assertEquals(mockPoint, particleEffect.getPoint());
    }

    @Test
    public void testSpawn() {
        @SuppressWarnings("unchecked")
        Point<Rotation> entityPoint = mock(Point.class);
        Position entityPosition = mock(Position.class);
        Location entityLocation = mock(Location.class);

        Entity entity = mock(Entity.class);
        when(entity.getLocation()).thenReturn(entityLocation);
        when(mockPoint.setPosition(entityPosition)).thenReturn(entityPoint);

        assertNotEquals(entityPoint, particleEffect.getPoint());

        try (MockedStatic<Position> mockPosition = mockStatic(Position.class)) {
            mockPosition.when(() -> Position.of(any(Location.class))).thenReturn(entityPosition);

            particleEffect.attach(entity);
            particleEffect.spawn();

            verify(mockPoint, times(1)).setPosition(entityPosition);
            assertEquals(entityPoint, particleEffect.getPoint());
        }
    }

    @Test
    public void testUpdateProperty() {
        assertEquals(Particle.REDSTONE, properties.getParticle());
        assertEquals(1, properties.getCount());
        assertEquals(new Vector(0, 0, 0), properties.getOffset());
        assertEquals(0, properties.getSpeed());
        assertTrue(properties.getData() instanceof Particle.DustOptions);

        particleEffect.updateProperty(ParticleProperty.TYPE, "Note");
        particleEffect.updateProperty(ParticleProperty.COUNT, "8");
        particleEffect.updateProperty(ParticleProperty.OFFSET, "3,3,3");
        particleEffect.updateProperty(ParticleProperty.SPEED, "4");
        particleEffect.updateProperty(ParticleProperty.COLOR, "012,123,234");

        assertEquals(Particle.NOTE, properties.getParticle());
        assertEquals(8, properties.getCount());
        assertEquals(new Vector(3, 3, 3), properties.getOffset());
        assertEquals(4, properties.getSpeed());
        assertEquals(12, properties.getData());

        particleEffect.updateProperty(ParticleProperty.TYPE, "Flame");

        assertEquals(Particle.FLAME, properties.getParticle());
        assertThrows(UnsupportedOperationException.class, () -> particleEffect.updateProperty(ParticleProperty.COLOR, "012,123,234"));
        assertThrows(UnsupportedOperationException.class, () -> particleEffect.updateProperty(ParticleProperty.LENGTH, "10"));
    }
}
