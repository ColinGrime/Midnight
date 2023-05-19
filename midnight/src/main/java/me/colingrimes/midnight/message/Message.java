package me.colingrimes.midnight.message;

import me.colingrimes.midnight.command.handler.util.Sender;
import me.colingrimes.midnight.message.implementation.ComponentMessage;
import me.colingrimes.midnight.message.implementation.ListMessage;
import me.colingrimes.midnight.message.implementation.TextMessage;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.List;

/**
 * A versatile and generic message interface that can be used in various contexts.
 *
 * @param <T> the type of the message content
 */
public interface Message<T> {

    /**
     * Factory method that generates a Message of type T.
     *
     * @param content the content for the Message, must be of type String or TextComponent.
     * @param <T>     the type of the content. Supported types are String and TextComponent.
     * @return a new Message of type T.
     * @throws IllegalArgumentException if the content is not of a supported type.
     */
    @SuppressWarnings("unchecked")
    static <T> Message<T> of(@Nonnull T content) {
        if (content instanceof String) {
            return (Message<T>) new TextMessage((String) content);
        } else if (content instanceof TextComponent) {
            return (Message<T>) new ComponentMessage((TextComponent) content);
        } else if (content instanceof List<?> contentList && contentList.get(0) instanceof String) {
            return (Message<T>) new ListMessage((List<String>) content);
        } else {
            throw new IllegalArgumentException("Unsupported message content type: " + content.getClass().getName());
        }
    }

    /**
     * Gets the content of the message.
     *
     * @return the content of the message
     */
    @Nonnull
    T getContent();

    /**
     * Converts the message content to a plain text format.
     *
     * @return the plain text representation of the message content
     */
    @Nonnull
    @SuppressWarnings("unchecked")
    default String toText() {
        T content = getContent();
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof TextComponent) {
            return ((TextComponent) content).toPlainText();
        }  else if (content instanceof List<?> && ((List<?>) content).get(0) instanceof String) {
            return String.join("\n", (List<String>) content);
        } else {
            return "";
        }
    }

    /**
     * Sends the message to a {@link CommandSender} recipient.
     *
     * @param recipient the command sender recipient of the message
     */
    void send(@Nonnull CommandSender recipient);

    /**
     * Sends the message to a {@link Player} recipient.
     *
     * @param recipient the player recipient of the message
     */
    default void send(@Nonnull Player recipient) {
        send((CommandSender) recipient);
    }

    /**
     * Sends the message to a {@link Sender} recipient.
     *
     * @param recipient the sender recipient of the message
     */
    default void send(@Nonnull Sender recipient) {
        send(recipient.handle());
    }

    /**
     * Applies {@link Placeholders} to the message and returns the result.
     *
     * @param placeholders the placeholders to apply to the message
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<?> replace(@Nonnull Placeholders placeholders) {
        return placeholders.apply(this);
    }


    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     *
     * @param key1   the placeholder key
     * @param value1 the placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<?> replace(@Nonnull String key1, @Nonnull Object value1) {
        return replace(Placeholders.of(key1, value1));
    }

    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     *
     * @param key1   the first placeholder key
     * @param value1 the first placeholder value
     * @param key2   the second placeholder key
     * @param value2 the second placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<?> replace(@Nonnull String key1, @Nonnull Object value1,
                               @Nonnull String key2, @Nonnull Object value2) {
        return replace(Placeholders.of(key1, value1).add(key2, value2));
    }

    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     *
     * @param key1   the first placeholder key
     * @param value1 the first placeholder value
     * @param key2   the second placeholder key
     * @param value2 the second placeholder value
     * @param key3   the third placeholder key
     * @param value3 the third placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<?> replace(@Nonnull String key1, @Nonnull Object value1,
                               @Nonnull String key2, @Nonnull Object value2,
                               @Nonnull String key3, @Nonnull Object value3) {
        return replace(Placeholders.of(key1, value1).add(key2, value2).add(key3, value3));
    }

    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     *
     * @param key1   the first placeholder key
     * @param value1 the first placeholder value
     * @param key2   the second placeholder key
     * @param value2 the second placeholder value
     * @param key3   the third placeholder key
     * @param value3 the third placeholder value
     * @param key4   the fourth placeholder key
     * @param value4 the fourth placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<?> replace(@Nonnull String key1, @Nonnull Object value1,
                               @Nonnull String key2, @Nonnull Object value2,
                               @Nonnull String key3, @Nonnull Object value3,
                               @Nonnull String key4, @Nonnull Object value4) {
        return replace(Placeholders.of(key1, value1).add(key2, value2).add(key3, value3).add(key4, value4));
    }
}
