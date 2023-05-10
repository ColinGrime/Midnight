package me.colingrimes.channels;

import me.colingrimes.channels.channel.Channel;
import me.colingrimes.channels.channel.Participant;
import me.colingrimes.channels.manager.ChannelManager;
import me.colingrimes.channels.message.ChannelMessage;
import me.colingrimes.midnight.scheduler.Scheduler;

import javax.annotation.Nonnull;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Used to interact with the MidnightChannels API.
 * To create custom channels, see {@link me.colingrimes.channels.channel.builder.ChannelBuilder}.
 */
public class ChannelAPI {

	/**
	 * Gets the channel manager.
	 * This is used to get, add, and remove {@code Channel}s and {@code Participant}s.
	 *
	 * @return channel manager
	 */
	@Nonnull
	public static ChannelManager getManager() {
		return MidnightChannels.getInstance().getChannelManager();
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelMessage}s sent in the given {@code Channel} during the last 24 hours.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param channel the channel to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelMessage<?>>> getLogsByChannel(@Nonnull Channel channel) {
		return getLogsByChannel(channel, ZonedDateTime.now().minusDays(1));
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelMessage}s sent in the given {@code Channel} after the specified start time.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param channel the channel to fetch logs for
	 * @param from    the start of the time period to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelMessage<?>>> getLogsByChannel(@Nonnull Channel channel, @Nonnull ZonedDateTime from) {
		return getLogsByChannel(channel, from, ZonedDateTime.now());
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelMessage}s sent in the given {@code Channel} between the specified start and end times.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param channel the channel to fetch logs for
	 * @param from    the start of the time period to fetch logs for
	 * @param to      the end of the time period to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelMessage<?>>> getLogsByChannel(@Nonnull Channel channel, @Nonnull ZonedDateTime from, @Nonnull ZonedDateTime to) {
		return Scheduler.ASYNC.call(() -> MidnightChannels.getInstance().getChatStorage().getLogsByChannel(channel, from, to));
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelMessage}s sent by the given {@code Participant} during the last 24 hours.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param participant the participant to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelMessage<?>>> getLogsByParticipant(@Nonnull Participant participant) {
		return getLogsByParticipant(participant, ZonedDateTime.now().minusDays(1));
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelMessage}s sent by the given {@code Participant} after the specified start time.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param participant the participant to fetch logs for
	 * @param from        the start of the time period to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelMessage<?>>> getLogsByParticipant(@Nonnull Participant participant, @Nonnull ZonedDateTime from) {
		return getLogsByParticipant(participant, from, ZonedDateTime.now());
	}

	/**
	 * Returns a {@link CompletableFuture} that, when completed, will contain a {@code List} of
	 * {@code ChannelMessage}s sent by the given {@code Participant} between the specified start and end times.
	 * <p>
	 * This operation is performed asynchronously to prevent blocking the main thread.
	 *
	 * @param participant the participant to fetch logs for
	 * @param from        the start of the time period to fetch logs for
	 * @param to          the end of the time period to fetch logs for
	 * @return a {@link CompletableFuture} that, when completed, contains the requested logs
	 */
	@Nonnull
	public static CompletableFuture<List<ChannelMessage<?>>> getLogsByParticipant(@Nonnull Participant participant, @Nonnull ZonedDateTime from, @Nonnull ZonedDateTime to) {
		return Scheduler.ASYNC.call(() -> MidnightChannels.getInstance().getChatStorage().getLogsByParticipant(participant, from, to));
	}
}
