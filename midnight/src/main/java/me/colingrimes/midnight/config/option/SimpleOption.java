package me.colingrimes.midnight.config.option;

import me.colingrimes.midnight.config.adapter.ConfigurationAdapter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class SimpleOption<T> implements Option<T> {

	private final Function<ConfigurationAdapter, ? extends T> function;
	private T value;

	public SimpleOption(@Nonnull Function<ConfigurationAdapter, ? extends T> function) {
		this.function = function;
		this.reload(null);
	}

	@Nonnull
	@Override
	public T get() {
		return value;
	}

	@Override
	public void reload(@Nullable ConfigurationAdapter adapter) {
		this.value = function.apply(adapter);
	}
}
