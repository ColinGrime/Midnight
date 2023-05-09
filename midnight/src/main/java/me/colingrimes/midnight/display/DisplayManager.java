package me.colingrimes.midnight.display;

import me.colingrimes.midnight.display.type.DisplayType;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class DisplayManager {

    private final Map<Player, Map<DisplayType, Display>> playerDisplays = new HashMap<>();

    /**
     * Gets the display element for the player.
     *
     * @param player the player to get the display element for
     * @param displayType the type of display element to get
     * @return the display element for the player
     */
    @Nonnull
    public Optional<Display> get(@Nonnull Player player, @Nonnull DisplayType displayType) {
        return Optional.ofNullable(playerDisplays.computeIfAbsent(player, k -> new HashMap<>()).get(displayType));
    }

    /**
     * Sets the display element for the player.
     *
     * @param player the player to set the display element for
     * @param display the display element to set
     */
    public void set(@Nonnull Player player, @Nonnull Display display) {
        playerDisplays.computeIfAbsent(player, k -> new HashMap<>()).put(display.getType(), display);
    }

    /**
     * Removes the display element from the player's display list.
     *
     * @param player the player to remove the display element from
     * @param displayType the type of display element to remove
     */
    public void remove(@Nonnull Player player, @Nonnull DisplayType displayType) {
        playerDisplays.computeIfAbsent(player, k -> new HashMap<>()).remove(displayType);
    }
}
