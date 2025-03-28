package me.colingrimes.midnight.config.util;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.menu.configurable.ConfigurableGui;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.misc.Types;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Represents a configurable inventory parsed from a configuration file.
 */
public class ConfigurableInventory {

    private final String title;
    private final int rows;
    private final Map<Integer, ItemStack> items;
    private final Map<Integer, String> commands;

    /**
     * Creates a configurable inventory.
     *
     * @param config the configuration section to parse
     * @return the configurable inventory
     */
    @Nonnull
    public static Optional<ConfigurableInventory> of(@Nullable ConfigurationSection config) {
        if (config == null) {
            return Optional.empty();
        }

        Preconditions.checkArgument(config.getString("title") != null || config.getString("name") != null, "Title is not set.");
        Preconditions.checkArgument(config.getString("rows") != null, "Rows are not set.");
        return Optional.of(new ConfigurableInventory(config));
    }

    private ConfigurableInventory(@Nonnull ConfigurationSection sec) {
        this.title = sec.getString("title") != null ? Text.color(sec.getString("title")) : Text.color(sec.getString("name"));
        this.rows = sec.getInt("rows");
        this.items = getItems(Objects.requireNonNull(sec.getConfigurationSection("slots")));
        this.commands = getCommands(Objects.requireNonNull(sec.getConfigurationSection("slots")));
        this.fillEmptySlots(sec);
    }

    /**
     * Gets the item stacks for each slot from the configuration section.
     *
     * @param sec the configuration section to parse
     * @return the item stack mapping
     */
    @Nonnull
    private Map<Integer, ItemStack> getItems(@Nonnull ConfigurationSection sec) {
        return Types.mapSlotKeys(sec, slot -> {
            if (sec.getString(slot + ".type") != null || sec.getString(slot + ".material") != null) {
                return Items.create().config(sec.getConfigurationSection(slot)).build();
            } else {
                return Items.create().build();
            }
        });
    }

    /**
     * Gets the commands for each slot from the configuration section.
     *
     * @param sec the configuration section to parse
     * @return the command mapping
     */
    @Nonnull
    private Map<Integer, String> getCommands(@Nonnull ConfigurationSection sec) {
        return Types.mapSlotKeys(sec, slot -> {
            String command = sec.getString(slot + ".command");
            return command != null ? command : "";
        });
    }

    /**
     * Fills empty slots in the inventory with the specified material.
     *
     * @param sec the configuration section to parse
     */
    private void fillEmptySlots(@Nonnull ConfigurationSection sec) {
        if (sec.getString("fill") == null) {
            return;
        }

        ItemStack item = Items.create()
                .material(sec.getString("fill"))
                .name("")
                .hide()
                .build();

        // Fill empty slots with the specified material.
        int total = rows * 9;
        for (int i=0; i<total; i++) {
			items.putIfAbsent(i, item);
        }
    }

    /**
     * Returns the title of the inventory.
     *
     * @return the title
     */
    @Nonnull
    public String getTitle() {
        return title;
    }

    /**
     * Returns the number of rows in the inventory.
     *
     * @return the rows
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the item stacks of each slot.
     *
     * @return the item stacks
     */
    @Nonnull
    public Map<Integer, ItemStack> getItems() {
        return items;
    }

    /**
     * Returns the command associated with the specified slot.
     *
     * @param slot the slot number
     * @return the command to run
     */
    @Nullable
    public String getCommand(int slot) {
        return commands.get(slot);
    }

    /**
     * Creates a simple GUI using the data from the inventory, then opens it for the player.
     *
     * @param player the player
     */
    public void open(@Nonnull Player player) {
        new ConfigurableGui(player, this).open();
    }
}
