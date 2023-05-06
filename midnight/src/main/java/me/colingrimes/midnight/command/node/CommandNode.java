package me.colingrimes.midnight.command.node;

import me.colingrimes.midnight.command.handler.CommandHandler;
import me.colingrimes.midnight.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a node in a tree-like structure for managing command hierarchies.
 * Responsible for handling command execution and tab completion.
 */
public final class CommandNode implements TabExecutor {

    private final CommandNode parent;
    private final Map<String, CommandNode> children = new HashMap<>();
    private final CommandHandler commandHandler;

    /**
     * Creates a new child command node.
     * @param parent the parent node
     */
    public CommandNode(@Nullable CommandNode parent) {
        this(parent, null);
    }

    /**
     * Creates a new child command node.
     * @param parent the parent node
     * @param commandHandler the command handler
     */
    public CommandNode(@Nullable CommandNode parent, @Nullable CommandHandler commandHandler) {
        this.parent = parent;
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
            Optional<Message<?>> usageMessage = findUsageMessage();
            if (usageMessage.isPresent()) {
                usageMessage.get().send(sender);
            } else {
                sender.sendMessage("Unknown command. Type \"/help\" for help.");
            }
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

    /**
     * Recursively searches for the first non-null usage message in the command node's hierarchy.
     * @return the first non-null usage message or null if none is found
     */
    @Nonnull
    private Optional<Message<?>> findUsageMessage() {
        if (commandHandler != null) {
            Message<?> usageMessage = commandHandler.getUsage();
            if (usageMessage != null) {
                return Optional.of(usageMessage);
            }
        }

        return parent != null ? parent.findUsageMessage() : Optional.empty();
    }
}
