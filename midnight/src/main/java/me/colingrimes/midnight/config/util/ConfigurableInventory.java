package me.colingrimes.midnight.config.util;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.util.bukkit.Items;
import me.colingrimes.midnight.util.text.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * A class representing a configurable inventory parsed from a configuration file.
 */
public class ConfigurableInventory {

    private final String title;
    private final ItemStack[] itemStacks;
    private final Map<Integer, String> commands;

    /**
     * Creates a configurable inventory.
     * @param sec the configuration section to parse
     * @return the configurable inventory
     */
    @Nonnull
    public static Optional<ConfigurableInventory> of(@Nullable ConfigurationSection sec) {
        if (sec == null) {
            return Optional.empty();
        }

        Preconditions.checkArgument(sec.getString("title") != null, "Title is not set.");
        Preconditions.checkArgument(sec.getString("rows") != null, "Rows are not set.");
        return Optional.of(new ConfigurableInventory(sec));
    }

    /**
     * Constructs a ConfigurableInventory instance from a ConfigurationSection.
     * @param sec the configuration section to parse
     */
    private ConfigurableInventory(@Nonnull ConfigurationSection sec) {
        this.title = parseTitle(sec);
        this.itemStacks = parseItemStacks(sec);
        this.commands = parseCommands(sec);
        this.fillEmptySlots(sec);
    }

    /**
     * Parses the title of the inventory from the configuration section.
     * @param sec the configuration section to parse
     * @return the title of the inventory
     */
    private String parseTitle(ConfigurationSection sec) {
        return Text.color(sec.getString("title"));
    }

    /**
     * Parses the ItemStacks for the inventory from the configuration section.
     * @param sec the configuration section to parse
     * @return an array of ItemStacks
     */
    private ItemStack[] parseItemStacks(ConfigurationSection sec) {
        ItemStack[] itemStacks = new ItemStack[sec.getInt("rows") * 9];
        ConfigurationSection slots = sec.getConfigurationSection("slots");

        // If there are no slots, return the empty array.
        if (slots == null) {
            return itemStacks;
        }

        // Parse each slot.
        for (String slot : slots.getKeys(false)) {
            itemStacks[Integer.parseInt(slot)] = Items.config(slots.getConfigurationSection(slot));
        }

        return itemStacks;
    }

    /**
     * Parses the commands for each slot from the configuration section.
     * @param sec the configuration section to parse
     * @return a map of commands associated with each slot
     */
    @Nonnull
    private Map<Integer, String> parseCommands(ConfigurationSection sec) {
        Map<Integer, String> commands = new HashMap<>();
        ConfigurationSection slots = sec.getConfigurationSection("slots");

        // If there are no slots, return the empty map.
        if (slots == null) {
            return commands;
        }

        // Parse each slot.
        for (String slot : slots.getKeys(false)) {
            commands.put(Integer.parseInt(slot), slots.getString(slot + ".command"));
        }

        return commands;
    }

    /**
     * Fills empty slots in the inventory with the specified material.
     * @param sec the configuration section to parse
     */
    private void fillEmptySlots(ConfigurationSection sec) {
        if (sec.getString("fill") == null) {
            return;
        }

        ItemStack item = new Items.ItemBuilder()
                .material(sec.getString("fill"))
                .name("")
                .build();

        // Fill empty slots with the specified material.
        for (int i=0; i<itemStacks.length; i++) {
            if (itemStacks[i] == null) {
                itemStacks[i] = item;
            }
        }
    }

    /**
     * Returns the title of the inventory.
     * @return the title
     */
    @Nonnull
    public String getTitle() {
        return title;
    }

    /**
     * Returns the number of rows in the inventory.
     * @return the rows
     */
    public int getRows() {
        return itemStacks.length / 9;
    }

    /**
     * Returns the ItemStacks in the inventory.
     * @return an array of ItemStacks
     */
    @Nonnull
    public ItemStack[] getItemStacks() {
        return itemStacks;
    }

    /**
     * Returns the command associated with the specified slot.
     * @param slot the slot number
     * @return the command to run
     */
    @Nonnull
    public Optional<String> getCommand(int slot) {
        return commands.get(slot).describeConstable();
    }
}
