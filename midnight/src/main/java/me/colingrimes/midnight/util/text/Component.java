package me.colingrimes.midnight.util.text;

import me.colingrimes.midnight.message.Message;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Component {

    private static final Pattern COLOR_CODES = Pattern.compile("^(?:§[0-9a-fk-or])*");

    /**
     * Converts a string to a TextComponent. If the string starts with a color code,
     * sets the color of the TextComponent to the corresponding color and removes
     * the color code from the string. Also applies all format codes it finds at the start of the string.
     *
     * @param text the string to be converted into a TextComponent
     * @return a TextComponent representing the text string, with color and format applied if specified in the string
     */
    @Nonnull
    public static TextComponent of(@Nonnull String text) {
        text = Text.color(text);

        Matcher matcher = COLOR_CODES.matcher(text);
        TextComponent component = new TextComponent();

        if (matcher.find()) {
            String codes = matcher.group();

            for (char codeChar : codes.toCharArray()) {
                if (codeChar == '§') {
                    continue;
                }

                if (ChatColor.getByChar(codeChar) != null) {
                    component.setColor(ChatColor.getByChar(codeChar));
                } else {
                    switch (codeChar) {
                        case 'k' -> component.setObfuscated(true);
                        case 'l' -> component.setBold(true);
                        case 'm' -> component.setStrikethrough(true);
                        case 'n' -> component.setUnderlined(true);
                        case 'o' -> component.setItalic(true);
                    }
                }
            }

            // Remove the color and format codes from the beginning of the string.
            text = text.replaceFirst(Pattern.quote(codes), "");
        }

        component.setText(text);
        return component;
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
        TextComponent mainComponent = null;

        // Split the text into parts, using the placeholder as the delimiter.
        component.setText(Text.color(component.getText()));
        Pattern placeholderColors = Pattern.compile("(?i)((?:§[0-9a-fk-or])*" + Pattern.quote(placeholder) + ")");
        Matcher matcher = placeholderColors.matcher(component.getText());

        // Get the text and the end of the last match.
        String text = component.getText();
        int endOfLastMatch = 0;

        while (matcher.find()) {
            String left = text.substring(endOfLastMatch, matcher.start());
            String placeholderWithColors = matcher.group();
            String colors = placeholderWithColors.substring(0, placeholderWithColors.length() - placeholder.length());

            // Create the component to the left of the placeholder.
            if (mainComponent == null) {
                mainComponent = new TextComponent(component);
                mainComponent.setText(left);
                mainComponent.setExtra(new ArrayList<>());
            } else {
                TextComponent leftComponent = Component.of(left);
                leftComponent.setClickEvent(component.getClickEvent());
                leftComponent.setHoverEvent(component.getHoverEvent());
                mainComponent.addExtra(leftComponent);
            }

            // Create the placeholder component.
            TextComponent placeholderComponent = null;

            if (value.getContent() instanceof String content) {
                placeholderComponent = Component.of(colors + content);
                placeholderComponent.setClickEvent(component.getClickEvent());
                placeholderComponent.setHoverEvent(component.getHoverEvent());
            } else if (value.getContent() instanceof TextComponent content) {
                TextComponent replacedComponent = replace(content, placeholder, value);
                placeholderComponent = Component.of(colors + replacedComponent.getText());
                placeholderComponent.setClickEvent(replacedComponent.getClickEvent());
                placeholderComponent.setHoverEvent(replacedComponent.getHoverEvent());
            } else if (value.getContent() instanceof List && ((List<?>) value.getContent()).get(0) instanceof String) {
                @SuppressWarnings("unchecked")
                List<String> content = (List<String>) value.getContent();

                placeholderComponent = Component.of(colors + String.join("\n", content));
                placeholderComponent.setClickEvent(component.getClickEvent());
                placeholderComponent.setHoverEvent(component.getHoverEvent());
            }

            // Add the component and set the new end.
            mainComponent.addExtra(placeholderComponent);
            endOfLastMatch = matcher.end();
        }

        // Create the main component or add the remaining text to the main component.
        if (mainComponent == null) {
            mainComponent = new TextComponent(component);
        } else if (endOfLastMatch != text.length()) {
            TextComponent lastComponent = Component.of(text.substring(endOfLastMatch));
            lastComponent.setClickEvent(component.getClickEvent());
            lastComponent.setHoverEvent(component.getHoverEvent());
            mainComponent.addExtra(lastComponent);
        }

        // Recursively replace placeholders in the extras of the original component.
        if (component.getExtra() != null) {
            for (BaseComponent baseComponent : component.getExtra()) {
                if (baseComponent instanceof TextComponent extraComponent) {
                    mainComponent.addExtra(replace(extraComponent, placeholder, value));
                } else {
                    mainComponent.addExtra(baseComponent);
                }
            }
        }

        replaceEvents(mainComponent, placeholder, value.toText());
        return mainComponent;
    }

    /**
     * Replaces all instances of the placeholder with the events of specified component.
     *
     * @param component   the component to replace the placeholder in
     * @param placeholder the placeholder
     * @param value       the value
     */
    public static void replaceEvents(@Nonnull TextComponent component, @Nonnull String placeholder, @Nonnull String value) {
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

    private Component() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
