package me.colingrimes.midnight.config.option;

import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.io.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Represents a configuration option for a configuration file.
 *
 * @param <T> type of the option
 */
public interface Option<T> {

	@Nonnull
	static <T> Option<T> of(@Nonnull Function<ConfigurationAdapter, T> function) {
		return new SimpleOption<>(function);
	}

	/**
	 * The value of the option.
	 *
	 * @return the value
	 */
	@Nonnull
	T get();

	/**
	 * Reloads the option from the given configuration adapter.
	 *
	 * @param adapter the adapter
	 */
	void reload(@Nullable ConfigurationAdapter adapter);

	/**
	 * Initializes all static fields of the given class that are of type {@link Option}.
	 *
	 * @param optionClass the class
	 * @return the list of options
	 */
	static List<Option<?>> initialize(Class<?> optionClass) {
		List<Option<?>> options = new ArrayList<>();
		for (Field field : optionClass.getFields()) {
			if (Modifier.isStatic(field.getModifiers()) && (Option.class.equals(field.getType()) || Message.class.equals(field.getType()))) {
				try {
					options.add((Option<?>) field.get(null));
				} catch (IllegalAccessException e) {
					Logger.severe("[Midnight] Option has failed to initialize an option:", e);
					throw new RuntimeException(e);
				}
			}
		}

		return options;
	}
}
