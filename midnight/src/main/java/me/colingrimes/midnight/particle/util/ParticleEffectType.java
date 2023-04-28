package me.colingrimes.midnight.particle.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;

/**
 * The currently supported particle effect types.
 */
public enum ParticleEffectType {
    CIRCLE;

    /**
     * Parses the provided string to the corresponding ParticleEffectType.
     * @param value the string value to parse
     * @return the ParticleEffectType matching the provided string
     */
    @Nonnull
    public static Optional<ParticleEffectType> fromString(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return Optional.empty();
        }

        for (ParticleEffectType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return Optional.of(type);
            }
        }

        return Optional.empty();
    }
}
