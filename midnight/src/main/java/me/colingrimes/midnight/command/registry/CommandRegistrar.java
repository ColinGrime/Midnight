package me.colingrimes.midnight.command.registry;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

public final class CommandRegistrar {

    /**
     * Dynamically registers the specified command.
     * @param plugin the plugin
     * @param name the name
     * @param command the command to register
     */
    public static void registerCommand(@Nonnull Plugin plugin, @Nonnull String name, @Nonnull CustomCommand command) {
        try {
            Field commandMapField = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);

            CommandMap commandMap = (CommandMap) commandMapField.get(Bukkit.getServer());
            commandMap.register(plugin.getName(), command);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private CommandRegistrar() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}