package me.colingrimes.particles.particle.util;

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
	POINTS(Integer::parseInt),   // Specific to CircleParticleEffect, SquareParticleEffect
	LENGTH(Double::parseDouble); // Specific to SquareParticleEffect

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
	 *
	 * @param value the string value to parse
	 * @return the ParticleProperty matching the provided string
	 */
	@Nonnull
	public static Optional<ParticleProperty> fromString(@Nullable String value) {
		return Parser.parse(ParticleProperty.class, value);
	}
}
