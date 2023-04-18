package me.colingrimes.midnight.command.util;

import me.colingrimes.midnight.locale.Messageable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * CommandProperties is a utility class used to store and manage properties of a command.
 * It contains fields for usage message, permission, aliases, arguments required, and
 * player requirement. This class provides getters and setters for each field, allowing
 * easy configuration of command properties.
 */
public class CommandProperties {

    private Messageable usage = null;
    private String permission = null;
    private String[] aliases = new String[]{};
    private int argumentsRequired = 0;
    private boolean playerRequired = false;

    @Nullable
    public Messageable getUsage() {
        return usage;
    }

    public void setUsage(@Nonnull Messageable usage) {
        this.usage = usage;
    }

    @Nullable
    public String getPermission() {
        return permission;
    }

    public void setPermission(@Nonnull String permission) {
        this.permission = permission;
    }

    @Nonnull
    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(@Nonnull String[] aliases) {
        this.aliases = aliases;
    }

    public int getArgumentsRequired() {
        return argumentsRequired;
    }

    public void setArgumentsRequired(int argumentsRequired) {
        this.argumentsRequired = argumentsRequired;
    }

    public boolean isPlayerRequired() {
        return playerRequired;
    }

    public void setPlayerRequired(boolean playerRequired) {
        this.playerRequired = playerRequired;
    }
}
