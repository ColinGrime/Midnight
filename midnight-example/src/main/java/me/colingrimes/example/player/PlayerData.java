package me.colingrimes.example.player;

import me.colingrimes.midnight.serialize.Serializable;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerData implements Serializable {

    private final UUID uuid;
    private int timesKilled = 0;

    public PlayerData(@Nonnull UUID uuid) {
        this.uuid = uuid;
    }

    public PlayerData(@Nonnull UUID uuid, int timesKilled) {
        this.uuid = uuid;
        this.timesKilled = timesKilled;
    }

    /**
     * Gets the UUID of the player.
     * @return the UUID
     */
    @Nonnull
    public UUID getUUID() {
        return uuid;
    }

    /**
     * Gets the number of times the player has been killed.
     * @return the number of times the player has been killed
     */
    public int getTimesKilled() {
        return timesKilled;
    }

    /**
     * Increments the number of times the player has been killed.
     * @return the new number of times the player has been killed
     */
    public int incrementTimesKilled() {
        return ++timesKilled;
    }

    @Nonnull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", uuid.toString());
        map.put("timesKilled", timesKilled);
        return map;
    }

    /**
     * Deserializes a {@link PlayerData} from a map.
     * @param map the map
     * @return the deserialized {@link PlayerData}
     */
    @Nonnull
    public static PlayerData deserialize(Map<String, Object> map) {
        UUID uuid = UUID.fromString((String) map.get("uuid"));
        int timesKilled = (int) map.get("timesKilled");
        return new PlayerData(uuid, timesKilled);
    }
}
