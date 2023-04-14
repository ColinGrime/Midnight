package me.colingrimes.midnight.command.node;

import me.colingrimes.midnight.command.registry.CommandRegistrar;
import me.colingrimes.midnight.command.registry.CustomCommand;
import me.colingrimes.midnight.plugin.MidnightPlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class CommandNode implements CommandExecutor, TabExecutor {

    private final Map<String, CommandNode> children = new HashMap<>();
    private CommandHandler commandHandler;

    /**
     * Creates a new root command node.
     * @param plugin the plugin
     * @param name the command name
     */
    public CommandNode(@Nonnull MidnightPlugin plugin, @Nonnull String name) {
        this(plugin, name, null);
    }

    /**
     * Creates a new root command node.
     * @param plugin the plugin
     * @param name the command name
     * @param commandHandler the command handler
     */
    public CommandNode(@Nonnull MidnightPlugin plugin, @Nonnull String name, @Nullable CommandHandler commandHandler) {
        CustomCommand customCommand = new CustomCommand(name, this, this);
        CommandRegistrar.registerCommand(plugin, customCommand);
        this.commandHandler = commandHandler;
    }

    /**
     * Creates a new child command node.
     */
    public CommandNode() {}

    /**
     * Creates a new child command node.
     * @param commandHandler the command handler
     */
    public CommandNode(@Nullable CommandHandler commandHandler) {
        this.commandHandler = commandHandler;
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        CommandNode child = null;
        if (args.length > 0) {
            child = children.get(args[0].toLowerCase());
        }

        // Invoke the current command handler if there are no more possible children.
        if (child == null && commandHandler != null) {
            commandHandler.invoke(sender, args);
        } else if (child != null) {
            child.onCommand(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (args.length == 1) {
            return children.keySet().stream()
                    .filter(child -> child.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length > 1) {
            CommandNode child = children.get(args[0].toLowerCase());
            if (child != null) {
                return child.onTabComplete(sender, command, label, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        return null;
    }

    /**
     * Gets the children of this command node.
     * @return the children
     */
    @Nonnull
    public Map<String, CommandNode> getChildren() {
        return children;
    }
}
