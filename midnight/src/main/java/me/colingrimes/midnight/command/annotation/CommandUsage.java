package me.colingrimes.midnight.command.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandUsage {

    /**
     * Gets the usage of the command.
     * @return the usage of the command
     */
    String value();
}