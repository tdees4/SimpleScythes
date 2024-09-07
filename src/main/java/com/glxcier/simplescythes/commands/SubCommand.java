package com.glxcier.simplescythes.commands;

import org.bukkit.command.CommandSender;

/**
 * The base structure of a subcommand of the scythe command.
 */
public abstract class SubCommand {

    private final String name;
    private final String description;
    private final String usage;
    private final String permission;

    public SubCommand(String name, String description, String usage, String permission) {
        this.name = name;
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    /**
     * The code to be executed when the subcommand is run.
     * @param sender The sender of the command.
     * @param args The arguments provided for the command.
     */
    public abstract void execute(CommandSender sender, String[] args);

    /**
     * @return The name of the subcommand.
     */
    public String getName() {
        return name;
    }

    /**
     * @return The description of the subcommand.
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return The usage of the subcommand.
     */
    public String getUsage() {
        return usage;
    }

    /**
     * @return The permission required to use the subcommand.
     */
    public String getPermission() {
        return permission;
    }

}
