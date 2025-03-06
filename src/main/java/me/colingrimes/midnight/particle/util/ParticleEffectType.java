package me.colingrimes.midnight.particle.util;

import me.colingrimes.midnight.util.text.Parser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * The currently supported particle effect types.
 */
public enum ParticleEffectType {
    CIRCLE,
    SQUARE;

    /**
     * Parses the provided string to the corresponding ParticleEffectType.
     *
     * @param value the string value to parse
     * @return the ParticleEffectType matching the provided string
     */
    @Nonnull
    public static Optional<ParticleEffectType> fromString(@Nullable String value) {
        return Parser.parse(ParticleEffectType.class, value);
    }
}
