package me.colingrimes.midnight.command.handler.factory;

import me.colingrimes.midnight.command.handler.AnnotationCommandHandler;

import javax.annotation.Nonnull;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public interface CommandHandlerFactory {

	/**
	 * Creates a new command handler.
	 * @param method the method
	 * @param permission the permission
	 * @param usageMessage the usage message
	 * @return the command method
	 */
	@Nonnull
	static AnnotationCommandHandler create(@Nonnull Method method, @Nonnull String permission, @Nonnull String usageMessage) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
		Object instance = method.getDeclaringClass().getDeclaredConstructor().newInstance();
		return new AnnotationCommandHandler(method, instance, permission, usageMessage);
	}
}
