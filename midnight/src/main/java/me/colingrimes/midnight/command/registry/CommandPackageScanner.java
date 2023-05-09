package me.colingrimes.midnight.command.registry;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.CommandHandler;
import me.colingrimes.midnight.util.io.Files;
import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Scans and registers command classes within the root command package of the plugin.
 */
public class CommandPackageScanner {

    private final Midnight plugin;
    private final CommandRegistry commandRegistry;

    public CommandPackageScanner(@Nonnull Midnight plugin, @Nonnull CommandRegistry commandRegistry) {
        this.plugin = plugin;
        this.commandRegistry = commandRegistry;
    }

    /**
     * Scans and registers command classes.
     */
    public void scan() {
        scan(plugin.getRootPackage() + ".command", "");
    }

    /**
     * Scans and registers commands from the specified package.
     *
     * @param packagePath the package path to scan for commands
     * @param commandPath the command path
     */
    private void scan(@Nonnull String packagePath, @Nonnull String commandPath) {
        List<Class<?>> classes = Files.getClasses(plugin, packagePath, false);
        classes = classes.stream().filter(Command.class::isAssignableFrom).toList();

        // Only 1 command class per package.
        if (classes.size() > 1) {
            Logger.severe("More than one command class found in package: " + packagePath);
            return;
        }

        // Get the new command path based on the next package.
        commandPath = getCommandPath(packagePath, commandPath);

        // Register the command class.
        if (classes.size() == 1) {
            registerCommand(plugin, commandPath, classes.get(0));
        } else if (!packagePath.equals(plugin.getRootPackage() + ".command")) {
            commandRegistry.register(commandPath.split(" "), null);
        }

        // Recursively check the sub-packages for sub-commands.
        for (String subPackage : Files.getPackageNames(plugin, packagePath)) {
            scan(packagePath + "." + subPackage, commandPath);
        }
    }

    /**
     * Generates and returns the updated command path based on the package path.
     *
     * @param packagePath the current package path being processed
     * @param commandPath the current command path being built
     * @return the updated command path.
     */
    @Nonnull
    private String getCommandPath(@Nonnull String packagePath, @Nonnull String commandPath) {
        // Ignore the root command package.
        if (packagePath.equals(plugin.getRootPackage() + ".command")) {
            return commandPath;
        }

        String[] packages = packagePath.split("\\.");
        String command = packages[packages.length - 1].toLowerCase();
        return commandPath.isEmpty() ? command : commandPath + " " + command;
    }

    /**
     * Registers the command class.
     *
     * @param plugin       the plugin to register the command class for
     * @param commandClass the command class to register
     * @param commandPath  the command path
     * @param <T>          the type of plugin
     */
    private <T extends Midnight> void registerCommand(@Nonnull T plugin, @Nonnull String commandPath, @Nonnull Class<?> commandClass) {
        Command<T> command = null;

        try {
            @SuppressWarnings("unchecked")
            Class<? extends Command<T>> clazz = (Class<? extends Command<T>>) commandClass;
            command = clazz.getDeclaredConstructor().newInstance();
        } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
            Logger.severe("Failed to register command: " + commandClass.getSimpleName());
            e.printStackTrace();
        }

        if (command != null) {
            commandRegistry.register(commandPath.split(" "), CommandHandler.create(plugin, command));
        }
    }
}
