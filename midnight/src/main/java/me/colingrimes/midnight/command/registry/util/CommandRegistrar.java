package me.colingrimes.midnight.command.registry.util;

import me.colingrimes.midnight.command.registry.CustomCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;

public final class CommandRegistrar {

    /**
     * Dynamically registers the specified command.
     * @param plugin the plugin
     * @param command the command to register
     */
    public static void registerCommand(@Nonnull Plugin plugin, @Nonnull CustomCommand command) {
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