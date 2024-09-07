package com.glxcier.simplescythes.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandUtils {

    /**
     * Send a message to the sender of a command.
     * @param sender The sender of the command.
     * @param message The message to send.
     */
    public static void messageSender(CommandSender sender, String message) {
        if (sender instanceof Player) {
            sender.sendMessage(message);
        } else {
            System.out.println(message);
        }
    }

}
