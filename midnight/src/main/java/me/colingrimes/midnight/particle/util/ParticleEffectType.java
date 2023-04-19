package me.colingrimes.midnight.particle.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The currently supported particle effect types.
 */
public enum ParticleEffectType {
    CIRCLE;

    /**
     * Parses the provided string to the corresponding ParticleEffectType.
     * Defaults to {@link #CIRCLE} if the provided string does not match any of the types.
     * @param value the string value to parse
     * @return the ParticleEffectType matching the provided string
     */
    @Nonnull
    public static ParticleEffectType fromString(@Nullable String value) {
        if (value == null || value.isEmpty()) {
            return CIRCLE;
        }

        for (ParticleEffectType type : values()) {
            if (type.name().equalsIgnoreCase(value)) {
                return type;
            }
        }

        return CIRCLE;
    }
}
