package me.colingrimes.midnight.particle.util;

import me.colingrimes.midnight.util.ParsingUtil;
import org.bukkit.Particle;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public enum ParticleProperty {

	PARTICLE_TYPE(s -> Particle.valueOf(s.toUpperCase())),
	COUNT(Integer::parseInt),
	OFFSET(ParsingUtil::parseVector),
	SPEED(Double::parseDouble),
	COLOR(ParsingUtil::parseColor),
	RADIUS(Double::parseDouble), // Specific to CircleParticleEffect
	POINTS(Integer::parseInt); // Specific to CircleParticleEffect

	private final Function<String, Object> valueParser;

	ParticleProperty(@Nonnull Function<String, Object> valueParser) {
		this.valueParser = valueParser;
	}

	@Nonnull
	public Object parseValue(@Nonnull String value) {
		return valueParser.apply(value);
	}

	/**
	 * Parses the provided string to the corresponding ParticleProperty.
	 * @param value the string value to parse
	 * @return the ParticleProperty matching the provided string
	 * @throws IllegalArgumentException if the string does not match any ParticleProperty
	 */
	public static ParticleProperty fromString(@Nullable String value) {
		if (value == null || value.isEmpty()) {
			throw new IllegalArgumentException("Value cannot be null or empty");
		}

		for (ParticleProperty type : values()) {
			if (type.name().equalsIgnoreCase(value)) {
				return type;
			}
		}

		throw new IllegalArgumentException("No matching ParticleProperty found for value: " + value);
	}
}
