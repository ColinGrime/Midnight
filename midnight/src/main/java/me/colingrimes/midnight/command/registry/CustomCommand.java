package me.colingrimes.midnight.command.registry;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CustomCommand extends Command {

    private final CommandExecutor commandExecutor;
    private final TabExecutor tabExecutor;

    public CustomCommand(@Nonnull String name, @Nonnull CommandExecutor commandExecutor, @Nonnull TabExecutor tabExecutor) {
        super(name);
        this.commandExecutor = commandExecutor;
        this.tabExecutor = tabExecutor;
    }

    @Override
    public boolean execute(@Nonnull CommandSender sender, @Nonnull String commandLabel, @Nonnull String[] args) {
        return commandExecutor.onCommand(sender, this, commandLabel, args);
    }

    @Nonnull
    @Override
    public List<String> tabComplete(@Nonnull CommandSender sender, @Nonnull String alias, @Nonnull String[] args) throws IllegalArgumentException {
        List<String> list = tabExecutor.onTabComplete(sender, this, alias, args);
        return list == null ? new ArrayList<>() : list;
    }
}
