package me.colingrimes.midnight.command.handler.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Optional;

public final class ArgumentParser {

    /**
     * Parses an argument into the specified type.
     * Supports: {@link String}, {@link Integer}, {@link Double}, {@link Player}
     * @param type the type to parse the argument into
     * @param arg the argument to parse
     * @return the parsed argument, or {@link Optional#empty()} if the argument could not be parsed
     */
    @Nonnull
    public static Optional<Object> parse(@Nonnull Class<?> type, @Nonnull String arg) {
        return parseString(type, arg)
                .or(() -> parseInt(type, arg))
                .or(() -> parseDouble(type, arg))
                .or(() -> parsePlayer(type, arg));
    }

    @Nonnull
    private static Optional<Object> parseString(@Nonnull Class<?> type, @Nonnull String arg) {
        return type == String.class ? Optional.of(arg) : Optional.empty();
    }

    @Nonnull
    private static Optional<Object> parseInt(@Nonnull Class<?> type, @Nonnull String arg) {
        return type == int.class || type == Integer.class ? parseInteger(arg) : Optional.empty();
    }

    @Nonnull
    private static Optional<Object> parseDouble(@Nonnull Class<?> type, @Nonnull String arg) {
        return type == double.class || type == Double.class ? parseDoubleValue(arg) : Optional.empty();
    }

    @Nonnull
    private static Optional<Object> parsePlayer(@Nonnull Class<?> type, @Nonnull String arg) {
        return type == Player.class ? Optional.ofNullable(Bukkit.getPlayer(arg)) : Optional.empty();
    }

    @Nonnull
    private static Optional<Object> parseInteger(@Nonnull String arg) {
        try {
            return Optional.of(Integer.parseInt(arg));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    @Nonnull
    private static Optional<Object> parseDoubleValue(@Nonnull String arg) {
        try {
            return Optional.of(Double.parseDouble(arg));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }

    private ArgumentParser() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
