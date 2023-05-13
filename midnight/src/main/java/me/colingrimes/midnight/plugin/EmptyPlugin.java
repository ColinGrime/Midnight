package me.colingrimes.midnight.plugin;

import me.colingrimes.midnight.Midnight;

/**
 * Represents the empty plugin that provides the Midnight library to other plugins.
 */
public class EmptyPlugin extends Midnight {

    @Override
    public void onLoad() {}

    @Override
    public void onEnable() {}

    @Override
    public void onDisable() {}
}
