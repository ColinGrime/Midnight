package me.colingrimes.channels.channel.misc;

import javax.annotation.Nullable;

/**
 * Enum representing different types of chat channels.
 */
public enum ChannelType {
	/**
	 * Represents a global chat channel, visible to all chatters.
	 */
	GLOBAL,
	/**
	 * Represents a private chat channel, typically used for direct messages between two chatters.
	 */
	PRIVATE,
	/**
	 * Represents a group chat channel, visible only to a specific group of chatters.
	 */
	GROUP,
	/**
	 * Represents a staff chat channel, visible only to staff members.
	 */
	STAFF;

	/**
	 * Returns the ChannelType value corresponding to the given string.
	 *
	 * @param name the name of the channel type
	 * @return the ChannelType value corresponding to the given string, or null if there is no matching value
	 */
	@Nullable
	public static ChannelType fromString(@Nullable String name) {
		for (ChannelType type : ChannelType.values()) {
			if (type.name().equalsIgnoreCase(name)) {
				return type;
			}
		}
		return null;
	}
}
