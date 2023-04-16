package me.colingrimes.midnight.command.registry.util;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.factory.CommandHandlerFactory;
import me.colingrimes.midnight.plugin.MidnightPlugin;
import me.colingrimes.midnight.util.Logger;
import me.colingrimes.midnight.util.clazz.ClassFinder;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

public final class CommandPackageScanner {

    public static <T extends MidnightPlugin> void scanAndRegister(@Nonnull T plugin) {
        scanAndRegister(plugin, plugin.getRootPackage() + ".command", "");
    }

    private static <T extends MidnightPlugin> void scanAndRegister(@Nonnull T plugin, @Nonnull String packageName, @Nonnull String commandPath) {
        List<Class<?>> classes = ClassFinder.getClasses(plugin, packageName, false);
        classes = classes.stream().filter(Command.class::isAssignableFrom).toList();

        // Only 1 command class per package.
        if (classes.size() > 1) {
            Logger.severe("More than one command class found in package: " + packageName);
            return;
        }

        // Register the command class.
        if (classes.size() == 1) {
            try {
                commandPath = register(plugin, commandPath, classes.get(0));
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                Logger.severe("Failed to register command: " + classes.get(0).getSimpleName());
                e.printStackTrace();
            }
        }

        // Recursively check the sub-packages for sub-commands.
        for (String subPackage : ClassFinder.getSubPackages(plugin, packageName)) {
            scanAndRegister(plugin, packageName + "." + subPackage, commandPath);
        }
    }

    @Nonnull
    private static <T extends MidnightPlugin> String register(@Nonnull T plugin, @Nonnull String commandPath, @Nonnull Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        @SuppressWarnings("unchecked")
        Command<T> command = ((Class<? extends Command<T>>) clazz).getDeclaredConstructor().newInstance();

        // Get the new command path.
        if (commandPath.isEmpty()) {
            commandPath = clazz.getSimpleName().toLowerCase();
        } else {
            commandPath += clazz.getSimpleName().replaceAll(".*[A-Z]", "").toLowerCase();
        }

        plugin.getCommandRegistry().register(commandPath.split(" "), CommandHandlerFactory.create(plugin, command));
        return commandPath;
    }

    private CommandPackageScanner() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
