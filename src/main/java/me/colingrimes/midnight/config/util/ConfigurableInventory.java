package me.colingrimes.midnight.config.util;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.menu.configurable.ConfigurableGui;
import me.colingrimes.midnight.util.bukkit.Items;
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
     * @param section the configuration section to parse
     * @return the configurable inventory
     */
    @Nonnull
    public static Optional<ConfigurableInventory> of(@Nullable ConfigurationSection section) {
        if (section == null) {
            return Optional.empty();
        }

        Preconditions.checkArgument(section.getString("title") != null || section.getString("name") != null, "Title is not set.");
        Preconditions.checkArgument(section.getString("rows") != null, "Rows are not set.");
        return Optional.of(new ConfigurableInventory(section));
    }

    private ConfigurableInventory(@Nonnull ConfigurationSection section) {
        this.title = section.getString("title") != null ? Text.color(section.getString("title")) : Text.color(section.getString("name"));
        this.rows = section.getInt("rows");
        this.items = getItems(Objects.requireNonNull(section.getConfigurationSection("slots")));
        this.commands = getCommands(Objects.requireNonNull(section.getConfigurationSection("slots")));
        this.fillEmptySlots(section);
    }

    /**
     * Gets the item stacks for each slot from the configuration section.
     *
     * @param section the configuration section to parse
     * @return the item stack mapping
     */
    @Nonnull
    private Map<Integer, ItemStack> getItems(@Nonnull ConfigurationSection section) {
        return Configs.mapSlotKeys(section, sec -> {
            if (sec.getString("type") != null || section.getString("material") != null) {
                return Items.create().config(sec).build();
            } else {
                return Items.create().build();
            }
        });
    }

    /**
     * Gets the commands for each slot from the configuration section.
     *
     * @param section the configuration section to parse
     * @return the command mapping
     */
    @Nonnull
    private Map<Integer, String> getCommands(@Nonnull ConfigurationSection section) {
        return Configs.mapSlotKeys(section, sec -> {
            String command = sec.getString("command");
            return command != null ? command : "";
        });
    }

    /**
     * Fills empty slots in the inventory with the specified material.
     *
     * @param section the configuration section to parse
     */
    private void fillEmptySlots(@Nonnull ConfigurationSection section) {
        if (section.getString("fill") == null) {
            return;
        }

        ItemStack item = Items.create()
                .material(section.getString("fill"))
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
