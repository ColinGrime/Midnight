package me.colingrimes.example.player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private final Map<UUID, PlayerData> playerData = new HashMap<>();

    /**
     * Loads the player data for the given UUID.
     * @param uuid the UUID
     * @param playerData the player data
     */
    public void loadPlayerData(@Nonnull UUID uuid, @Nonnull PlayerData playerData) {
        this.playerData.put(uuid, playerData);
    }

    /**
     * Gets the player data for the given UUID.
     * @param uuid the UUID
     * @return the player data
     */
    @Nonnull
    public PlayerData getPlayerData(@Nonnull UUID uuid) {
        return playerData.computeIfAbsent(uuid, PlayerData::new);
    }
}
