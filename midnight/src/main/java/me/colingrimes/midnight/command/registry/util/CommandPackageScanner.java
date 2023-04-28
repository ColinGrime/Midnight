package me.colingrimes.midnight.command.registry.util;

import me.colingrimes.midnight.MidnightPlugin;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.factory.CommandHandlerFactory;
import me.colingrimes.midnight.util.ClassFinder;
import me.colingrimes.midnight.util.Logger;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * Scans and registers command classes within the root command package of the plugin.
 */
public final class CommandPackageScanner {

    /**
     * Scans and registers command classes for the given plugin.
     * @param plugin the plugin to register command classes for
     * @param <T> the type of the MidnightPlugin
     */
    public static <T extends MidnightPlugin> void scanAndRegister(@Nonnull T plugin) {
        scanAndRegister(plugin, plugin.getRootPackage() + ".command", "");
    }

    /**
     * Scans and registers commands from the specified package.
     * @param plugin the plugin instance
     * @param packagePath the package path to scan for commands
     * @param commandPath the command path
     * @param <T> the type of MidnightPlugin
     */
    private static <T extends MidnightPlugin> void scanAndRegister(@Nonnull T plugin, @Nonnull String packagePath, @Nonnull String commandPath) {
        List<Class<?>> classes = ClassFinder.getClasses(plugin, packagePath, false);
        classes = classes.stream().filter(Command.class::isAssignableFrom).toList();

        // Only 1 command class per package.
        if (classes.size() > 1) {
            Logger.severe("More than one command class found in package: " + packagePath);
            return;
        }

        // Get the new command path based on the next package.
        commandPath = getCommandPath(plugin, packagePath, commandPath);

        // Register the command class.
        if (classes.size() == 1) {
            registerCommand(plugin, commandPath, classes.get(0));
        } else if (!packagePath.equals(plugin.getRootPackage() + ".command")) {
            plugin.getCommandRegistry().register(commandPath.split(" "), null);
        }

        // Recursively check the sub-packages for sub-commands.
        for (String subPackage : ClassFinder.getSubPackages(plugin, packagePath)) {
            scanAndRegister(plugin, packagePath + "." + subPackage, commandPath);
        }
    }

    /**
     * Generates and returns the updated command path based on the package path.
     * @param plugin the plugin instance
     * @param packagePath the current package path being processed
     * @param commandPath the current command path being built
     * @return the updated command path.
     * @param <T> the type of the MidnightPlugin
     */
    @Nonnull
    private static <T extends MidnightPlugin> String getCommandPath(@Nonnull T plugin, @Nonnull String packagePath, @Nonnull String commandPath) {
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
     * @param plugin the plugin to register the command class for
     * @param commandClass the command class to register
     * @param commandPath the command path
     * @param <T> the type of the MidnightPlugin
     */
    private static <T extends MidnightPlugin> void registerCommand(@Nonnull T plugin, @Nonnull String commandPath, @Nonnull Class<?> commandClass) {
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
            plugin.getCommandRegistry().register(commandPath.split(" "), CommandHandlerFactory.create(plugin, command));
        }
    }

    private CommandPackageScanner() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
