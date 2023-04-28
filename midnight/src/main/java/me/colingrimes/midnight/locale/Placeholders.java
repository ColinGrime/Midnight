package me.colingrimes.midnight.locale;

import me.colingrimes.midnight.util.text.Text;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Provides quick and efficient replacement of multiple placeholders within a string or list of strings.
 */
public class Placeholders {

	private final Map<String, String> placeholders = new HashMap<>();

	/**
	 * Creates a new placeholders object.
	 * @return the placeholders object
	 */
	public static Placeholders create() {
		return new Placeholders();
	}

	/**
	 * Creates a new placeholders object with the placeholder and replacement.
	 * @param placeholder the placeholder you want to add
	 * @param replacement the value you want to replace the placeholder with
	 * @return the placeholders object
	 * @param <T> any type
	 */
	public static <T> Placeholders of(@Nonnull String placeholder, @Nonnull T replacement) {
		return new Placeholders(placeholder, replacement);
	}

	private Placeholders() {}

	private <T> Placeholders(@Nonnull String placeholder, @Nonnull T replacement) {
		Objects.requireNonNull(placeholder, "Placeholder is null.");
		Objects.requireNonNull(replacement, "Replacement is null.");
		placeholders.put(placeholder, String.valueOf(replacement));
	}

	/**
	 * Adds a placeholder to the list of placeholders.
	 * @param placeholder the placeholder you want to add
	 * @param replacement the value you want to replace the placeholder with
	 * @param <T> any type
	 * @return the placeholders object
	 */
	public @Nonnull <T> Placeholders add(@Nonnull String placeholder, @Nonnull T replacement) {
		Objects.requireNonNull(placeholder, "Placeholder is null.");
		Objects.requireNonNull(replacement, "Replacement is null.");
		placeholders.put(placeholder, String.valueOf(replacement));
		return this;
	}

	/**
	 * Replaces all placeholders in a string with the replacement value.
	 * @param str the string to replace placeholders with
	 * @return the new string with replaced placeholders
	 */
	public @Nonnull String replace(@Nullable String str) {
		if (str == null) {
			return "";
		}

		// Replace the placeholders in the string.
		for (Map.Entry<String, String> replacement : placeholders.entrySet()) {
			str = str.replace(replacement.getKey(), replacement.getValue());
		}

		return Text.color(str);
	}

	/**
	 * Replaces all placeholders in all strings with the replacement value.
	 * @param strList the list of strings to replace placeholders with
	 * @return the new list of strings with replaced placeholders
	 */
	public @Nonnull List<String> replace(@Nullable List<String> strList) {
		if (strList == null) {
			return new ArrayList<>();
		}

		return strList.stream().map(this::replace).collect(Collectors.toList());
	}

	/**
	 * Replaces all placeholders in a component with the replacement value.
	 * @param component the component to replace placeholders with
	 * @return the new component with replaced placeholders
	 */
	public @Nonnull TextComponent replace(@Nullable TextComponent component) {
		if (component == null) {
			return new TextComponent();
		}

		// Replace the placeholders in the main component.
		TextComponent mainComponent = new TextComponent(replace(component.getText()));
		mainComponent.setClickEvent(component.getClickEvent());
		mainComponent.setHoverEvent(component.getHoverEvent());

		if (component.getExtra() == null) {
			return mainComponent;
		}

		// Replace the placeholders in the extra components.
		for (BaseComponent base : component.getExtra()) {
			if (!(base instanceof TextComponent extraComponent)) {
				mainComponent.addExtra(base);
				continue;
			}

			TextComponent newTextComponent = new TextComponent(replace(extraComponent.getText()));
			newTextComponent.setClickEvent(extraComponent.getClickEvent());
			newTextComponent.setHoverEvent(extraComponent.getHoverEvent());
			mainComponent.addExtra(newTextComponent);
		}

		return mainComponent;
	}
}
