package me.colingrimes.midnight.util.text;

import me.colingrimes.midnight.message.implementation.ComponentMessage;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * Tooltip is a utility class that provides an easy way to add multiple lines of hoverable text (tooltips) to a message.
 */
public final class Tooltip {

    /**
     * Creates a {@link ComponentMessage} with hoverable text.
     *
     * @param message  the message to be displayed
     * @param tooltips the list of strings to be added as hoverable text
     * @return a TextComponent with the message and hoverable text
     */
    @Nonnull
    public static ComponentMessage create(@Nonnull String message, @Nonnull List<String> tooltips) {
        TextComponent component = new TextComponent(message);
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.join("\n", tooltips))));
        return new ComponentMessage(component);
    }

    /**
     * Creates a {@link ComponentMessage} with hoverable text and a command to suggest when the text is clicked.
     *
     * @param message  the message to be displayed
     * @param tooltips the list of strings to be added as hoverable text
     * @param command  the command to be run when the text is clicked
     * @return a TextComponent with the message and hoverable text
     */
    @Nonnull
    public static ComponentMessage create(@Nonnull String message, @Nonnull List<String> tooltips, @Nonnull String command) {
        TextComponent component = new TextComponent(message);
        component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(String.join("\n", tooltips))));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command));
        return new ComponentMessage(component);
    }

    private Tooltip() {
        throw new UnsupportedOperationException("This class cannot be instantiated.");
    }
}
