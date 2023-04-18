package me.colingrimes.midnight.command.handler;

import me.colingrimes.midnight.command.handler.util.ArgumentParser;
import me.colingrimes.midnight.locale.Messageable;
import me.colingrimes.midnight.locale.SimpleMessage;
import me.colingrimes.plugin.config.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Optional;

/**
 * A command handler that uses reflection to execute annotated command methods.
 * Handles permission checking, argument parsing, and command execution.
 */
public class ReflectiveCommandHandler implements CommandHandler {

    private final Method method;
    private final Parameter[] parameters;
    private final Object instance;
    private final String permission;
    private final Messageable usageMessage;

    public ReflectiveCommandHandler(@Nonnull Method method, @Nonnull Object instance, @Nullable String permission, @Nullable String usageMessage) {
        this.method = method;
        this.parameters = method.getParameters();
        this.instance = instance;
        this.permission = permission;
        this.usageMessage = usageMessage == null ? null : new SimpleMessage(usageMessage);
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull org.bukkit.command.Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (!sender.hasPermission(permission)) {
            Messages.PERMISSION_DENIED.sendTo(sender);
            return true;
        }

        Object[] convertedArgs = parseArguments(sender, args);
        if (convertedArgs == null) {
            if (getUsage() != null) {
                getUsage().sendTo(sender);
                return true;
            }
            return false;
        }

        try {
            method.invoke(instance, convertedArgs);
        } catch (IllegalAccessException | InvocationTargetException e) {
            return false;
        }

        return true;
    }

    @Nullable
    @Override
    public Messageable getUsage() {
        return usageMessage;
    }

    /**
     * Parses the command arguments into the correct types.
     * @param sender the command sender
     * @param args the command arguments
     * @return the parsed arguments, or null if the arguments could not be parsed
     */
    private Object[] parseArguments(@Nonnull CommandSender sender, @Nonnull String[] args) {
        // Arguments do not match the method parameters, return null.
        if (parameters.length != args.length + 1) {
            return null;
        }

        Object[] convertedArgs = new Object[parameters.length];
        convertedArgs[0] = sender;

        // Check if sender argument is valid.
        if (parameters[0].getType() == Player.class && !(sender instanceof Player)) {
            return null;
        }

        // Parse the remaining arguments.
        for (int i=1; i<parameters.length; i++) {
            Class<?> type = parameters[i].getType();
            Optional<Object> parsedArg = ArgumentParser.parse(type, args[i - 1]);

            // Argument cannot be parsed, return null.
            if (parsedArg.isEmpty()) {
                return null;
            } else {
                convertedArgs[i] = parsedArg.get();
            }
        }

        return convertedArgs;
    }
}
