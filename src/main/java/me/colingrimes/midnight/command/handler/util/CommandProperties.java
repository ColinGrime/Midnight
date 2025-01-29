package me.colingrimes.midnight.command.handler.util;

import me.colingrimes.midnight.message.Message;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Represents a utility class for managing command properties, including usage message,
 * permission, aliases, number of required arguments, and player requirement.
 */
public class CommandProperties {

    private Message<?> usage = null;
    private String permission = null;
    private String[] aliases = new String[]{};
    private int argumentsRequired = 0;
    private boolean playerRequired = false;

    /**
     * Gets the command usage message.
     *
     * @return the usage message, or null if not set
     */
    @Nullable
    public Message<?> getUsage() {
        return usage;
    }

    /**
     * Sets the command usage message.
     *
     * @param usage the usage message to set
     */
    public void setUsage(@Nonnull Message<?> usage) {
        this.usage = usage;
    }

    /**
     * Gets the command permission.
     *
     * @return the command permission, or null if not set
     */
    @Nullable
    public String getPermission() {
        return permission;
    }

    /**
     * Sets the command permission.
     *
     * @param permission the permission to set
     */
    public void setPermission(@Nonnull String permission) {
        this.permission = permission;
    }

    /**
     * Gets the command aliases.
     *
     * @return an array of command aliases
     */
    @Nonnull
    public String[] getAliases() {
        return aliases;
    }

    /**
     * Sets the command aliases.
     *
     * @param aliases an array of aliases to set
     */
    public void setAliases(@Nonnull String...aliases) {
        this.aliases = aliases;
    }

    /**
     * Gets the number of required arguments for the command.
     *
     * @return the number of required arguments
     */
    public int getArgumentsRequired() {
        return argumentsRequired;
    }

    /**
     * Sets the number of required arguments for the command.
     *
     * @param argumentsRequired the number of required arguments to set
     */
    public void setArgumentsRequired(int argumentsRequired) {
        this.argumentsRequired = argumentsRequired;
    }

    /**
     * Checks if a player is required for the command execution.
     *
     * @return true if a player is required, false otherwise
     */
    public boolean isPlayerRequired() {
        return playerRequired;
    }

    /**
     * Sets the player requirement for the command execution.
     *
     * @param playerRequired true if a player is required, false otherwise
     */
    public void setPlayerRequired(boolean playerRequired) {
        this.playerRequired = playerRequired;
    }
}
