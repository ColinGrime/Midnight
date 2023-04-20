package me.colingrimes.midnight.particle.util;

import me.colingrimes.midnight.util.text.Parser;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Function;

public enum ParticleProperty {

	TYPE(Parser::parseParticle),
	COUNT(Integer::parseInt),
	OFFSET(Parser::parseVector),
	SPEED(Double::parseDouble),
	COLOR(Parser::parseColor),
	RADIUS(Double::parseDouble), // Specific to CircleParticleEffect
	POINTS(Integer::parseInt); // Specific to CircleParticleEffect

	private final Function<String, Object> valueParser;

	ParticleProperty(@Nonnull Function<String, Object> valueParser) {
		this.valueParser = valueParser;
	}

	@Nullable
	public Object parseValue(@Nonnull String value) {
		return valueParser.apply(value);
	}

	/**
	 * Parses the provided string to the corresponding ParticleProperty.
	 * @param value the string value to parse
	 * @return the ParticleProperty matching the provided string
	 */
	@Nonnull
	public static Optional<ParticleProperty> fromString(@Nullable String value) {
		if (value == null || value.isEmpty()) {
			return Optional.empty();
		}

		for (ParticleProperty type : values()) {
			if (type.name().equalsIgnoreCase(value)) {
				return Optional.of(type);
			}
		}

		return Optional.empty();
	}
}
