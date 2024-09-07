package com.glxcier.simplescythes.commands.subcommands;

import com.glxcier.simplescythes.commands.CommandUtils;
import com.glxcier.simplescythes.commands.ScytheCommand;
import com.glxcier.simplescythes.commands.SubCommand;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * A subcommand that lists all subcommands of the scythe command.
 */
public class HelpCommand extends SubCommand {

    private final ScytheCommand mainCommand;

    public HelpCommand(ScytheCommand mainCommand) {
        super("help", "Displays a list of SimpleScythe commands.", "/scythe help <page>", "scythe.admin");
        this.mainCommand = mainCommand;
    }

    /**
     * Displays a list of commands related to the Simple Scythes plugin.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {

        List<SubCommand> commandList = mainCommand.getSubCommands();

        int maxPages = (commandList.size() / 7) + 1;
        int currentPage;
        int pageLength = 7;

        if (args.length == 0) {
            currentPage = 1;
        } else {
            try {
                currentPage = Integer.parseInt(args[0]);
                if (currentPage > maxPages) {
                    currentPage = maxPages;
                }
            } catch (NumberFormatException e) {
                CommandUtils.messageSender(sender, ChatColor.RED + "Invalid page number!");
                return;
            }
        }

        CommandUtils.messageSender(sender, ChatColor.translateAlternateColorCodes('&', "&a&lSimpleScythes Help Menu (" + currentPage + "/" + maxPages + ")"));
        for (int i = 0; i < pageLength; i++) {
            int listIndex = i + pageLength * (currentPage - 1);
            if (listIndex >= commandList.size()) {
                break;
            }
            SubCommand command = commandList.get(listIndex);
            CommandUtils.messageSender(sender, ChatColor.translateAlternateColorCodes('&', "&a" + command.getUsage() + " &3- &7" + command.getDescription()));
        }

    }
}
