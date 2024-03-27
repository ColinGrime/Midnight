package me.colingrimes.midnight.command.node;

import me.colingrimes.midnight.command.handler.CommandHandler;
import me.colingrimes.midnight.message.Message;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Represents a node in a tree-like structure for managing command hierarchies,
 * handling command execution and tab completion.
 * <p>
 * CommandNode instances can have children, representing subcommands or
 * related commands, and a parent to refer back to the higher command hierarchy.
 * The parent node is used to find usage messages when a command fails to execute
 * and there is no usage message defined in the current node, effectively
 * traversing up the command hierarchy until a suitable usage message is found.
 * <p>
 * The class also holds a reference to a {@link CommandHandler} for executing
 * and tab completing commands.
 */
public final class CommandNode implements TabExecutor {

    private final CommandNode parent;
    private final Map<String, CommandNode> children = new HashMap<>();
    private final CommandHandler commandHandler;

    /**
     * Creates a new child command node with the specified parent.
     *
     * @param parent the parent node
     */
    public CommandNode(@Nullable CommandNode parent) {
        this(parent, null);
    }

    /**
     * Creates a new child command node with the specified parent and command handler.
     *
     * @param parent         the parent node
     * @param commandHandler the command handler for executing and tab completing commands
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
            String commandFailed = "Unknown command. Type \"/help\" for help.";
            findUsageMessage().ifPresentOrElse(m -> m.send(sender), () -> sender.sendMessage(commandFailed));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        // At least 2 arguments are needed so that it makes sense to tab complete the sub command.
        if (args.length > 1) {
            CommandNode child = children.get(args[0].toLowerCase());
            if (child != null) {
                return child.onTabComplete(sender, cmd, label, Arrays.copyOfRange(args, 1, args.length));
            }
        }

        // Delegate tab completion to the command handler if it exists.
        List<String> completions = commandHandler == null ? null : commandHandler.onTabComplete(sender, cmd, label, args);
        if (completions != null) {
            return completions;
        }

        // Tab complete with the child command nodes if nothing else is found.
        if (args.length == 1) {
            List<String> found = children.keySet().stream().filter(c -> c.startsWith(args[0].toLowerCase())).toList();
            return found.isEmpty() ? null : found;
        }

        return null;
    }

    /**
     * Retrieves the child command nodes of this command node.
     *
     * @return a map containing the child command nodes, with the command
     *         name as the key and the associated CommandNode as the value
     */
    @Nonnull
    public Map<String, CommandNode> getChildren() {
        return children;
    }

    /**
     * Retrieves the command handler associated with this command node.
     *
     * @return the command handler
     */
    @Nullable
    public CommandHandler getHandler() {
        return commandHandler;
    }

    /**
     * Recursively searches for the first non-null usage message in the command node's hierarchy.
     * The search starts with the current command node and moves up through its ancestors.
     *
     * @return an {@link Optional} containing the first non-null usage message found,
     *         or an empty {@link Optional} if none is found
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
