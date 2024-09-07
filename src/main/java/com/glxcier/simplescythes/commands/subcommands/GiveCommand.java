package com.glxcier.simplescythes.commands.subcommands;

import com.glxcier.simplescythes.commands.CommandUtils;
import com.glxcier.simplescythes.commands.SubCommand;
import com.glxcier.simplescythes.tools.ScytheHandler;
import com.glxcier.simplescythes.tools.ScytheMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand extends SubCommand {

    private final ScytheHandler scytheHandler;

    public GiveCommand(ScytheHandler scytheHandler) {
        super("give", "Gives the player a scythe.", "/scythe give <player> <type> <amount>", "scythe.admin");
        this.scytheHandler = scytheHandler;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {

        if (args.length < 2) {
            CommandUtils.messageSender(sender, ChatColor.RED + "Not enough arguments! Use the command like: " + getUsage());
            return;
        }

        String playerName = args[0];
        String type = args[1];
        int amount = 1;

        if (args.length >= 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                CommandUtils.messageSender(sender, ChatColor.RED + "Invalid amount!");
                return;
            }
        }

        Player player = Bukkit.getPlayer(playerName);

        if (player == null) {
            CommandUtils.messageSender(sender, ChatColor.RED + "Invalid player!");
            return;
        }

        ScytheMaterial material = ScytheMaterial.fromString(type);

        if (material == null) {
            if (sender instanceof Player) {
                sender.sendMessage(ChatColor.RED + "Invalid type!");
            } else {
                System.out.println("Invalid type!");
            }
            return;
        }

        for (int i = 0; i < amount; i++) {
            player.getInventory().addItem(scytheHandler.getScythe(material));
        }

    }

}
