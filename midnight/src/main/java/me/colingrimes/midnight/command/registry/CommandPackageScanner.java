package me.colingrimes.midnight.command.registry;

import me.colingrimes.midnight.Midnight;
import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.CommandHandler;
import me.colingrimes.midnight.command.handler.StandardCommandHandler;
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
     * <p>
     * Only classes in the "command" package will be processed.
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
        // Get all Command classes.
        List<Class<?>> classes = Files.getClasses(plugin, packagePath, false);
        classes = classes.stream().filter(Command.class::isAssignableFrom).toList();

        // Only 1 command class per package is allowed.
        if (classes.size() > 1) {
            Logger.severe("More than one command class found in package: " + packagePath);
            return;
        }

        // Get the command to register.
        String command = getCommand(packagePath);
        String newCommandPath = commandPath.isEmpty() ? command : commandPath + " " + command;

        // Register the command class and get the aliases.
        String[] aliases = new String[]{};
        if (classes.size() == 1) {
            aliases = registerCommand(plugin, newCommandPath, classes.get(0));
        } else if (!packagePath.equals(plugin.getRootPackage() + ".command")) {
            commandRegistry.register(newCommandPath.split(" "), null);
        }

        // Recursively check the sub-packages for sub-commands.
        for (String subPackage : Files.getPackageNames(plugin, packagePath)) {
            scan(packagePath + "." + subPackage, newCommandPath);

            // Register the aliases of the command.
            for (String alias : aliases) {
                String aliasPath = commandPath.isEmpty() ? alias : commandPath + " " + alias;
                scan(packagePath + "." + subPackage, aliasPath);
            }
        }
    }

    /**
     * Gets the command from the package path.
     *
     * @param packagePath the current package path being processed
     * @return the command from the package path
     */
    @Nonnull
    private String getCommand(@Nonnull String packagePath) {
        // Ignore the root command package.
        if (packagePath.equals(plugin.getRootPackage() + ".command")) {
            return "";
        }

        String[] packages = packagePath.split("\\.");
        return packages[packages.length - 1].toLowerCase();
    }

    /**
     * Registers the command class.
     *
     * @param plugin       the plugin to register the command class for
     * @param commandClass the command class to register
     * @param commandPath  the command path
     * @param <T>          the type of plugin
     * @return the aliases of the command
     */
    @Nonnull
    private <T extends Midnight> String[] registerCommand(@Nonnull T plugin, @Nonnull String commandPath, @Nonnull Class<?> commandClass) {
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
            StandardCommandHandler<T> handler = CommandHandler.create(plugin, command);
            String[] args = commandPath.split(" ");
            commandRegistry.register(args, handler);

            // Register the command aliases.
            for (String alias : handler.getProperties().getAliases()) {
                args[args.length - 1] = alias;
                commandRegistry.register(args, handler);
            }

            return handler.getProperties().getAliases();
        }

        return new String[]{};
    }
}
