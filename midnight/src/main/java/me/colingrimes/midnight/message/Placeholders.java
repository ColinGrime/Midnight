package me.colingrimes.midnight.message;

import me.colingrimes.midnight.util.text.Text;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A utility class designed for managing and applying multiple placeholders
 * within different types of input, including strings, lists of strings, or components.
 */
public class Placeholders {

	private final Map<String, String> placeholders = new HashMap<>();

	/**
	 * Creates a new placeholders object.
	 * @return the placeholders object
	 */
	@Nonnull
	public static Placeholders create() {
		return new Placeholders();
	}

	/**
	 * Creates a new placeholders object.
	 * @param placeholder the placeholder you want to add
	 * @param value the value you want to replace the placeholder with
	 * @return the placeholders object
	 * @param <T> any type
	 */
	@Nonnull
	public static <T> Placeholders of(@Nonnull String placeholder, @Nonnull T value) {
		return new Placeholders().add(placeholder, value);
	}

	/**
	 * Adds a placeholder to the list of placeholders.
	 * @param placeholder the placeholder you want to add
	 * @param value the value you want to replace the placeholder with
	 * @return the placeholders object
	 * @param <T> any type
	 */
	@Nonnull
	public <T> Placeholders add(@Nonnull String placeholder, @Nonnull T value) {
		placeholders.put(placeholder, String.valueOf(value));
		return this;
	}

	/**
	 * Applies all placeholders in a string.
	 * @param str the string to apply placeholders to
	 * @return the new string with applied placeholders
	 */
	@Nonnull
	public String apply(@Nullable String str) {
		if (str == null) {
			return "";
		}

		// Apply the placeholders in the string.
		for (Map.Entry<String, String> replacement : placeholders.entrySet()) {
			str = str.replace(replacement.getKey(), replacement.getValue());
		}

		return Text.color(str);
	}

	/**
	 * Applies all placeholders in all strings.
	 * @param strList the list of strings to apply placeholders to
	 * @return the new list of strings with applied placeholders
	 */
	@Nonnull
	public List<String> apply(@Nullable List<String> strList) {
		if (strList == null) {
			return new ArrayList<>();
		}

		return strList.stream().map(this::apply).collect(Collectors.toList());
	}

	/**
	 * Applies all placeholders in a component.
	 * @param component the component to apply placeholders to
	 * @return the new component with applied placeholders
	 */
	@Nonnull
	public TextComponent apply(@Nullable TextComponent component) {
		if (component == null) {
			return new TextComponent();
		}

		// Apply the placeholders in the main component.
		TextComponent mainComponent = new TextComponent(apply(component.getText()));
		mainComponent.setClickEvent(component.getClickEvent());
		mainComponent.setHoverEvent(component.getHoverEvent());

		if (component.getExtra() == null) {
			return mainComponent;
		}

		// Apply the placeholders in the extra components.
		for (BaseComponent base : component.getExtra()) {
			if (!(base instanceof TextComponent extraComponent)) {
				mainComponent.addExtra(base);
				continue;
			}

			TextComponent newTextComponent = new TextComponent(apply(extraComponent.getText()));
			newTextComponent.setClickEvent(extraComponent.getClickEvent());
			newTextComponent.setHoverEvent(extraComponent.getHoverEvent());
			mainComponent.addExtra(newTextComponent);
		}

		return mainComponent;
	}
}
