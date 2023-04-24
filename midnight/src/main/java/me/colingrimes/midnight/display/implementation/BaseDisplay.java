package me.colingrimes.midnight.display.implementation;

import me.colingrimes.midnight.display.Display;

abstract class BaseDisplay implements Display {

    int priority = DEFAULT_PRIORITY;

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }
}
