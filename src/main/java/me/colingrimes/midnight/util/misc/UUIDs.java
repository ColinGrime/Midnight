package me.colingrimes.midnight.util.misc;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import me.colingrimes.midnight.util.bukkit.Players;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class UUIDs {

	private static final Map<String, UUID> uuidCache = new HashMap<>();

	/**
	 * Attempts to retrieve the UUID of a player from their name.
	 *
	 * @param name the name of the player
	 * @return the UUID of the player
	 */
	@Nonnull
	public static Optional<UUID> fromName(@Nonnull String name) {
		if (uuidCache.containsKey(name)) {
			return Optional.ofNullable(uuidCache.get(name));
		}

		Optional<Player> player = Players.get(name);
		if (player.isPresent()) {
			return Optional.of(uuidCache.computeIfAbsent(name, k -> player.get().getUniqueId()));
		}

		// Attempts to retrieve the uuid from the Mojang API.
		try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL("https://api.mojang.com/users/profiles/minecraft/" + name).openStream()))) {
			JsonElement idElement = ((JsonObject) JsonParser.parseReader(in)).get("id");
			if (idElement != null) {
				return Optional.of(uuidCache.computeIfAbsent(name, k -> getUUID(idElement.toString())));
			}
		} catch (Exception ignored) {}

		return Optional.empty();
	}

	/**
	 * Gets the UUID from a string.
	 *
	 * @param uuidString the string to get the UUID from
	 * @return the UUID
	 */
	@Nonnull
	private static UUID getUUID(@Nonnull String uuidString) {
		return UUID.fromString(uuidString.replaceAll("\"", "").replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5"));
	}

	private UUIDs() {
		throw new UnsupportedOperationException("This class cannot be instantiated.");
	}
}