package me.colingrimes.midnight.message;

import me.colingrimes.midnight.channel.Participant;
import me.colingrimes.midnight.command.util.Sender;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * A versatile and generic message interface that can be used in various contexts.
 * @param <T> the type of the message content
 */
public interface Message<T> {

    /**
     * Gets the content of the message.
     * @return the content of the message
     */
    @Nonnull
    T getContent();

    /**
     * Converts the message content to a plain text format.
     * @return the plain text representation of the message content
     */
    @Nonnull
    default String toText() {
        T content = getContent();
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof TextComponent) {
            return ((TextComponent) content).toPlainText();
        } else {
            return "";
        }
    }

    /**
     * Sends the message to a {@link CommandSender} recipient.
     * @param recipient the command sender recipient of the message
     */
    void send(@Nonnull CommandSender recipient);

    /**
     * Sends the message to a {@link Player} recipient.
     * @param recipient the player recipient of the message
     */
    default void send(@Nonnull Player recipient) {
        send((CommandSender) recipient);
    }

    /**
     * Sends the message to a {@link Sender} recipient.
     * @param recipient the sender recipient of the message
     */
    default void send(@Nonnull Sender recipient) {
        send(recipient.handle());
    }

    /**
     * Sends the message to a {@link Participant} recipient.
     * @param recipient the participant recipient of the message
     */
    default void send(@Nonnull Participant recipient) {
        send(recipient.player());
    }

    /**
     * Applies {@link Placeholders} to the message and returns the result.
     * @param placeholders the placeholders to apply to the message
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    Message<T> replace(@Nonnull Placeholders placeholders);

    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     * @param key1 the placeholder key
     * @param value1 the placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<T> replace(@Nonnull String key1, @Nonnull Object value1) {
        return replace(Placeholders.of(key1, value1));
    }

    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     * @param key1 the first placeholder key
     * @param value1 the first placeholder value
     * @param key2 the second placeholder key
     * @param value2 the second placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<T> replace(@Nonnull String key1, @Nonnull Object value1,
                               @Nonnull String key2, @Nonnull Object value2) {
        return replace(Placeholders.of(key1, value1).add(key2, value2));
    }

    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     * @param key1 the first placeholder key
     * @param value1 the first placeholder value
     * @param key2 the second placeholder key
     * @param value2 the second placeholder value
     * @param key3 the third placeholder key
     * @param value3 the third placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<T> replace(@Nonnull String key1, @Nonnull Object value1,
                               @Nonnull String key2, @Nonnull Object value2,
                               @Nonnull String key3, @Nonnull Object value3) {
        return replace(Placeholders.of(key1, value1).add(key2, value2).add(key3, value3));
    }

    /**
     * Applies the provided placeholder key-value pair to the message and returns the result.
     * @param key1 the first placeholder key
     * @param value1 the first placeholder value
     * @param key2 the second placeholder key
     * @param value2 the second placeholder value
     * @param key3 the third placeholder key
     * @param value3 the third placeholder value
     * @param key4 the fourth placeholder key
     * @param value4 the fourth placeholder value
     * @return a new message with the placeholders replaced
     */
    @Nonnull
    default Message<T> replace(@Nonnull String key1, @Nonnull Object value1,
                               @Nonnull String key2, @Nonnull Object value2,
                               @Nonnull String key3, @Nonnull Object value3,
                               @Nonnull String key4, @Nonnull Object value4) {
        return replace(Placeholders.of(key1, value1).add(key2, value2).add(key3, value3).add(key4, value4));
    }
}
