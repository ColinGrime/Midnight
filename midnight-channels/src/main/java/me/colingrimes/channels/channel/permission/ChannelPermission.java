package me.colingrimes.channels.channel.permission;

/**
 * Represents permissions for different actions in a channel.
 */
public enum ChannelPermission {
    /**
     * Permission to join a channel.
     */
    JOIN,

    /**
     * Permission to leave a channel.
     */
    LEAVE,

    /**
     * Permission to send messages in a channel.
     */
    SEND,

    /**
     * Permission to mute/unmute other participants in a channel.
     */
    MUTE;
}
