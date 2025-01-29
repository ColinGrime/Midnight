package me.colingrimes.midnight.util.text;

import com.google.common.base.Preconditions;
import me.colingrimes.midnight.message.Message;
import me.colingrimes.midnight.util.misc.Types;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Component {

    private static final Pattern COLOR_CODES = Pattern.compile("^(?:§[0-9a-fk-or])*");

    /**
     * Creates a new component and colors it properly.
     *
     * @param text the text of the component
     * @return the newly created component
     */
    @Nonnull
    public static TextComponent of(@Nonnull String text) {
        return color(new TextComponent(), text);
    }

    /**
     * Creates a new component and copies the specified component's formatting.
     *
     * @param text the text of the component
     * @param component the component to copy formatting from
     * @return the newly created component
     */
    @Nonnull
    public static TextComponent of(@Nonnull String text, @Nonnull TextComponent component) {
        return color(copy(component), text);
    }

    /**
     * Creates a new {@link Component.Builder} object.
     *
     * @param text the text of the initial component
     * @return the component builder object
     */
    @Nonnull
    public static Builder create(@Nonnull String text) {
        return new Builder(text);
    }

    /**
     * Colors a TextComponent with the specified color codes.
     * <p>
     * If the string starts with a color code, sets the color of the TextComponent to
     * the corresponding color and removes the color code from the string.
     * <p>
     * Also applies all format codes it finds at the start of the string.
     *
     * @param component the TextComponent to color
     * @param textWithColorCodes the text with color codes to apply to the TextComponent
     * @return the colored TextComponent
     */
    @Nonnull
    public static TextComponent color(@Nonnull TextComponent component, @Nonnull String textWithColorCodes) {
        textWithColorCodes = Text.color(textWithColorCodes);
        Matcher matcher = COLOR_CODES.matcher(Text.color(textWithColorCodes));

        if (matcher.find()) {
            String codes = matcher.group();

            for (char codeChar : codes.toCharArray()) {
                if (codeChar == '§') {
                    continue;
                }

                switch (codeChar) {
                    case 'k' -> component.setObfuscated(true);
                    case 'l' -> component.setBold(true);
                    case 'm' -> component.setStrikethrough(true);
                    case 'n' -> component.setUnderlined(true);
                    case 'o' -> component.setItalic(true);
                    default -> component.setColor(ChatColor.getByChar(codeChar));
                }
            }

            // Remove the color and format codes from the beginning of the string.
            textWithColorCodes = textWithColorCodes.replaceFirst(Pattern.quote(codes), "");
        }

        component.setText(textWithColorCodes);
        return component;
    }

    /**
     * Makes a copy of the specified component (excludes extra components).
     *
     * @param component the component to copy
     * @return a copy of the component
     */
    public static TextComponent copy(@Nonnull TextComponent component) {
        TextComponent copy = new TextComponent(component.getText());
        copy.setColor(component.getColor());
        copy.setBold(component.isBold());
        copy.setItalic(component.isItalic());
        copy.setUnderlined(component.isUnderlined());
        copy.setStrikethrough(component.isStrikethrough());
        copy.setObfuscated(component.isObfuscated());
        copy.setClickEvent(component.getClickEvent());
        copy.setHoverEvent(component.getHoverEvent());
        return copy;
    }

    /**
     * Cleans the component's formatting.
     * This is necessary in order to stop the component from inheriting from their parent component.
     *
     * @param component the component to clean
     * @return the clean component
     */
    @Nonnull
    public static BaseComponent clean(@Nonnull BaseComponent component) {
        if (component.getColor() == ChatColor.WHITE) component.setColor(ChatColor.WHITE);
        if (!component.isBold()) component.setBold(false);
        if (!component.isItalic()) component.setItalic(false);
        if (!component.isUnderlined()) component.setUnderlined(false);
        if (!component.isStrikethrough()) component.setStrikethrough(false);
        if (!component.isObfuscated()) component.setObfuscated(false);
        if (component.getClickEvent() == null) component.setClickEvent(null);
        if (component.getHoverEvent() == null) component.setHoverEvent(null);
        if (component.getExtra() == null) component.setExtra(new ArrayList<>());
        return component;
    }

    /**
     * Flattens the component's extra contents.
     *
     * @param component the component to flatten
     * @return the flattened component
     */
    public static TextComponent flatten(@Nonnull TextComponent component) {
        TextComponent root = copy(component);
        if (component.getExtra() != null) {
            for (BaseComponent baseComponent : component.getExtra()) {
                if (baseComponent instanceof TextComponent) {
                    flattenComponent(root, (TextComponent) baseComponent);
                }
            }
        }
        return root;
    }

    /**
     * Flattens the component's extra contents.
     *
     * @param target the resulting flattened component
     * @param source the source component to flatten
     */
    private static void flattenComponent(@Nonnull TextComponent target, @Nullable TextComponent source) {
        if (source == null) {
            return;
        }

        target.addExtra(clean(copy(source)));

        if (source.getExtra() != null) {
            for (BaseComponent baseComponent : source.getExtra()) {
                if (baseComponent instanceof TextComponent) {
                    flattenComponent(target, (TextComponent) baseComponent);
                }
            }
        }
    }

    /**
     * Replaces all instances of the placeholder with the value in the specified component.
     *
     * @param component   the component to replace the placeholder in
     * @param placeholder the placeholder
     * @param value       the value
     * @return the component with the placeholder replaced
     */
    @Nonnull
    public static TextComponent replace(@Nonnull TextComponent component, @Nonnull String placeholder, @Nonnull Message<?> value) {
        component.setText(Text.color(component.getText()));

        TextComponent newComponent;
        if (value.getContent() instanceof String text) {
            newComponent = color(copy(component), component.getText().replace(placeholder, text));
        } else if (value.getContent() instanceof TextComponent) {
            newComponent = replaceComponent(component, placeholder, value);
        } else if (Types.asStringList(value.getContent()).isPresent()) {
            List<String> text = Types.asStringList(value.getContent()).get();
            newComponent = color(copy(component), component.getText().replace(placeholder, String.join(" ", text)));
        } else {
            throw new IllegalArgumentException("Value is an unknown type.");
        }

        // Recursively replace placeholders in the extras of the original component.
        if (component.getExtra() != null) {
            for (BaseComponent baseComponent : component.getExtra()) {
                if (baseComponent instanceof TextComponent extraComponent) {
                    newComponent.addExtra(clean(replace(extraComponent, placeholder, value)));
                } else {
                    newComponent.addExtra(clean(baseComponent));
                }
            }
        }

        replaceEvents(newComponent, placeholder, value.toText());
        return flatten(newComponent);
    }

    /**
     * Replaces all instances of the placeholder with the component value in the specified component.
     *
     * @param component      the component to replace the placeholder in
     * @param placeholder    the placeholder
     * @param componentValue the component value
     * @return the component with the placeholder replaced
     */
    @Nonnull
    private static TextComponent replaceComponent(@Nonnull TextComponent component, @Nonnull String placeholder, @Nonnull Message<?> componentValue) {
        if (!(componentValue.getContent() instanceof TextComponent content)) {
            throw new IllegalArgumentException("Value must be of type TextComponent.");
        }

        TextComponent newComponent = null;

        // Split the text into parts, using the placeholder as the delimiter.
        Pattern placeholderColors = Pattern.compile("(?i)((?:§[0-9a-fk-or])*" + Pattern.quote(placeholder) + ")");
        Matcher matcher = placeholderColors.matcher(component.getText());

        // Get the text and the end of the last match.
        String text = component.getText();
        int endOfLastMatch = 0;

        while (matcher.find()) {
            String left = text.substring(endOfLastMatch, matcher.start());
            newComponent = addText(component, newComponent, left);

            // Replace the placeholders in the placeholders.
            TextComponent placeholderComponent = replace(content, placeholder, componentValue);
            Component.color(placeholderComponent, placeholderComponent.getText());

            // Add the component and set the new end.
            if (newComponent == null) {
                newComponent = placeholderComponent;
            } else {
                newComponent.addExtra(placeholderComponent);
            }

            endOfLastMatch = matcher.end();
        }

        // Add the remaining text to the new component.
        newComponent = addText(component, newComponent, text.substring(endOfLastMatch));
        return newComponent == null ? new TextComponent() : newComponent;
    }

    /**
     * Replaces all instances of the placeholder with the events of specified component.
     *
     * @param component   the component to replace the placeholder in
     * @param placeholder the placeholder
     * @param value       the value
     */
    private static void replaceEvents(@Nonnull TextComponent component, @Nonnull String placeholder, @Nonnull String value) {
        // Replace the placeholder in the main text of the component.
        component.setText(Text.color(component.getText()).replace(placeholder, value));

        // Replace the placeholder in the hover event.
        if (component.getHoverEvent() != null) {
            HoverEvent hoverEvent = component.getHoverEvent();
            List<Content> contents = new ArrayList<>();

            for (Content content : hoverEvent.getContents()) {
                if (!(content instanceof net.md_5.bungee.api.chat.hover.content.Text text)) {
                    contents.add(content);
                    continue;
                } else if (text.getValue() instanceof String str) {
                    String replaced = Text.color(str.replace(placeholder, value));
                    contents.add(new net.md_5.bungee.api.chat.hover.content.Text(replaced));
                    continue;
                } else if (!(text.getValue() instanceof BaseComponent[])) {
                    contents.add(content);
                    continue;
                }

                BaseComponent[] baseComponents = (BaseComponent[]) text.getValue();
                for (BaseComponent baseComponent : baseComponents) {
                    if (baseComponent instanceof TextComponent textComponent) {
                        replaceEvents(textComponent, placeholder, value);
                    }
                }
                contents.add(new net.md_5.bungee.api.chat.hover.content.Text(baseComponents));
            }

            component.setHoverEvent(new HoverEvent(hoverEvent.getAction(), contents));
        }

        // Replace the placeholder in the click event.
        if (component.getClickEvent() != null) {
            ClickEvent clickEvent = component.getClickEvent();
            if (clickEvent.getAction() == ClickEvent.Action.RUN_COMMAND || clickEvent.getAction() == ClickEvent.Action.SUGGEST_COMMAND) {
                component.setClickEvent(new ClickEvent(clickEvent.getAction(), clickEvent.getValue().replace(placeholder, value)));
            }
        }
    }

    /**
     * Adds the text to a newly created component or adds it to an existing one.
     * <p>
     * If the new component has not yet been made, creates a new {@link TextComponent}
     * with the specified text and identical component formattings.
     *
     * @param component    the original component
     * @param newComponent the new component being made
     * @param text         the text that is being added
     * @return the new component after processing
     */
    @Nullable
    private static TextComponent addText(@Nonnull TextComponent component, @Nullable TextComponent newComponent, @Nonnull String text) {
        if (text.isEmpty()) {
            return newComponent;
        } else if (newComponent == null) {
            return Component.of(text, component);
        } else {
            newComponent.addExtra(Component.of(text, component));
            return newComponent;
        }
    }

    /**
     * Provides a clean way to build {@link TextComponent} objects.
     */
    public static class Builder {

        private final List<String> textList = new ArrayList<>();

        public Builder(@Nonnull String text) {
            textList.add(text);
        }

        /**
         * Text to be added to the TextComponent;
         *
         * @param text text to add
         * @return the component builder object
         */
        @Nonnull
        public Builder add(@Nonnull String text) {
            textList.add(text);
            return this;
        }

        /**
         * Builds the {@link TextComponent} object.
         *
         * @return the text component
         */
        @Nonnull
        public TextComponent build() {
            TextComponent component = null;
            for (int i=0; i<textList.size(); i++) {
                String newLine = i != textList.size() - 1 ? "\n" : "";
                if (component == null) {
                    component = of(textList.get(i) + newLine);
                } else {
                    component.addExtra(clean(of(textList.get(i) + newLine)));
                }
            }

            Preconditions.checkNotNull(component, "Built component is null.");
            return component;
        }
    }

    private Component() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
