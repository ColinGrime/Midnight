package me.colingrimes.midnight.command.node;

import me.colingrimes.midnight.command.handler.CommandHandler;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents a node in a tree-like structure for managing command hierarchies.
 * Responsible for handling command execution and tab completion.
 */
public final class CommandNode implements TabExecutor {

    private final Map<String, CommandNode> children = new HashMap<>();
    private CommandHandler commandHandler;

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
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        CommandNode child = null;
        if (args.length > 0) {
            child = children.get(args[0].toLowerCase());
        }

        if (child != null) {
            child.onCommand(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
        } else if (commandHandler == null || !commandHandler.onCommand(sender, cmd, label, args)) {
            sender.sendMessage("Unknown command. Type \"/help\" for help.");
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        List<String> completions = commandHandler == null ? null : commandHandler.onTabComplete(sender, cmd, label, args);
        if (completions != null) {
            return completions;
        }

        if (args.length == 1) {
            return children.keySet().stream()
                    .filter(child -> child.startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList());
        }

        if (args.length > 1) {
            CommandNode child = children.get(args[0].toLowerCase());
            if (child != null) {
                return child.onTabComplete(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
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
