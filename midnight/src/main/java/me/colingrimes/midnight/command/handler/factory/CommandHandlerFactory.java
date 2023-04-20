package me.colingrimes.midnight.command.handler.factory;

import me.colingrimes.midnight.command.Command;
import me.colingrimes.midnight.command.handler.CommandHandler;
import me.colingrimes.midnight.command.handler.ReflectiveCommandHandler;
import me.colingrimes.midnight.command.handler.StandardCommandHandler;
import me.colingrimes.midnight.MidnightPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * The {@code CommandHandlerFactory} is a utility interface that provides static factory methods
 * for creating {@code CommandHandler} instances. It simplifies the creation of both
 * {@code StandardCommandHandler} and {@code ReflectiveCommandHandler} instances.
 *
 * <p>Example usage:</p>
 * <pre>
 * {@code
 * CommandHandler standardHandler = CommandHandlerFactory.create(command);
 * CommandHandler reflectiveHandler = CommandHandlerFactory.create(method, permission, usageMessage);
 * }
 * </pre>
 */
public interface CommandHandlerFactory {

	/**
	 * Creates a new command handler.
	 * @param command the command
	 * @return the command handler
	 */
	@Nonnull
	static <T extends MidnightPlugin> CommandHandler create(@Nonnull T plugin, @Nonnull Command<T> command) {
		return new StandardCommandHandler<>(plugin, command);
	}

	/**
	 * Creates a new command handler.
	 * @param method the method
	 * @param permission the permission
	 * @param usageMessage the usage message
	 * @return the command method
	 */
	@Nonnull
	static CommandHandler create(@Nonnull Method method, @Nullable String permission, @Nullable String usageMessage) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
		return new ReflectiveCommandHandler(method, instance, permission, usageMessage);
	}
}
