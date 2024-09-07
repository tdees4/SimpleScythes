package com.glxcier.simplescythes.commands;

import com.glxcier.simplescythes.commands.subcommands.GiveCommand;
import com.glxcier.simplescythes.commands.subcommands.HelpCommand;
import com.glxcier.simplescythes.tools.ScytheHandler;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public class ScytheCommand implements CommandExecutor {

    private final List<SubCommand> subCommands;

    public ScytheCommand(ScytheHandler scytheHandler) {
        subCommands = Arrays.asList(
                new GiveCommand(scytheHandler),
                new HelpCommand(this)
        );
    }

    /**
     * Determines the subcommand being called and runs it.
     */
    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (strings.length > 0) {
            for (SubCommand subCommand : subCommands) {
                if (subCommand.getName().equalsIgnoreCase(strings[0])) {
                    if (commandSender instanceof Player && subCommand.getPermission() != null &&
                            !commandSender.hasPermission(subCommand.getPermission()) && !commandSender.isOp()) {
                        continue;
                    }

                    subCommand.execute(commandSender, Arrays.copyOfRange(strings, 1, strings.length));
                    return true;
                }
            }
        }
        CommandUtils.messageSender(commandSender, ChatColor.RED + "Improper command! Try using /scythe help.");
        return true;
    }

    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

}
