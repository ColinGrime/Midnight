package me.colingrimes.channels;

import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.ChannelFactory;
import me.colingrimes.channels.channel.chatter.Chatter;
import me.colingrimes.channels.filter.ChatFilterType;
import me.colingrimes.channels.filter.ChatFilters;
import me.colingrimes.channels.manager.ChatManager;
import me.colingrimes.channels.message.ChannelLog;
import me.colingrimes.midnight.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Used to interact with the MidnightChannels API.
 * To create custom channels, see {@link me.colingrimes.channels.channel.ChannelFactory}.
 * To interact with the global channel, see {@link #global()}.
 */
public class ChannelAPI {

	private static final Channel GLOBAL_CHANNEL;
	private static final ChatFilters FILTERS = new ChatFilters();
	private static final ChannelFactory channelFactory = new ChannelFactory();

	static {
		GLOBAL_CHANNEL = channelFactory.createGlobalChannel("Global");
		GLOBAL_CHANNEL.disable();

		FILTERS.add(ChatFilterType.PROFANITY);
		FILTERS.add(ChatFilterType.ADVERTISE);
		FILTERS.add(ChatFilterType.SPAM);
		FILTERS.add(ChatFilterType.FLOOD);
	}

	/**
	 * Returns the global channel.
	 * This channel is used to send messages to all players on the server.
	 * <p>
	 * <b>By default, this channel is disabled.</b>
	 *
	 * @return the global channel
	 */
	@Nonnull
	public static Channel global() {
		return GLOBAL_CHANNEL;
	}

	/**
	 * Returns the {@link ChatFilters} used to filter messages.
	 * This contains all the default filters.
	 *
	 * @return the chat filters
	 */
	@Nonnull
	public static ChatFilters filters() {
		return FILTERS;
	}

	/**
	 * Returns the {@link ChannelFactory} used to create custom channels.
	 *
	 * @return the channel factory
	 */
	@Nonnull
	public static ChannelFactory factory() {
		return channelFactory;
	}

	/**
	 * Gets the channel manager.
	 * This is used to get, add, and remove {@code Channel}s and {@code Chatter}s.
	 *
	 * @return channel manager
	 */
	@Nonnull
	public static ChatManager getManager() {
		return MidnightChannels.getInstance().getChannelManager();
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelLog}s sent in the given {@code Channel} during the last 24 hours.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param channel the channel to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelLog<?>>> getLogsByChannel(@Nonnull Channel channel) {
		return getLogsByChannel(channel, ZonedDateTime.now().minusDays(1));
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelLog}s sent in the given {@code Channel} after the specified start time.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param channel the channel to fetch logs for
	 * @param from    the start of the time period to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelLog<?>>> getLogsByChannel(@Nonnull Channel channel, @Nonnull ZonedDateTime from) {
		return getLogsByChannel(channel, from, ZonedDateTime.now());
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelLog}s sent in the given {@code Channel} between the specified start and end times.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param channel the channel to fetch logs for
	 * @param from    the start of the time period to fetch logs for
	 * @param to      the end of the time period to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelLog<?>>> getLogsByChannel(@Nonnull Channel channel, @Nonnull ZonedDateTime from, @Nonnull ZonedDateTime to) {
		return Scheduler.ASYNC.call(() -> MidnightChannels.getInstance().getChatLogStorage().getLogsByChannel(channel, from, to));
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelLog}s sent by the given {@code Chatter} during the last 24 hours.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param chatter the chatter to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelLog<?>>> getLogsByChatter(@Nonnull Chatter chatter) {
		return getLogsByChatter(chatter, ZonedDateTime.now().minusDays(1));
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelLog}s sent by the given {@code Chatter} after the specified start time.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param chatter the chatter to fetch logs for
	 * @param from        the start of the time period to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelLog<?>>> getLogsByChatter(@Nonnull Chatter chatter, @Nonnull ZonedDateTime from) {
		return getLogsByChatter(chatter, from, ZonedDateTime.now());
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelLog}s sent by the given {@code Chatter} between the specified start and end times.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param chatter the chatter to fetch logs for
	 * @param from        the start of the time period to fetch logs for
	 * @param to          the end of the time period to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelLog<?>>> getLogsByChatter(@Nonnull Chatter chatter, @Nonnull ZonedDateTime from, @Nonnull ZonedDateTime to) {
		return Scheduler.ASYNC.call(() -> MidnightChannels.getInstance().getChatLogStorage().getLogsByChatter(chatter, from, to));
	}
}
