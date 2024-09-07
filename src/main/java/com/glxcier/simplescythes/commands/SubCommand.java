package com.glxcier.simplescythes.commands;

import org.bukkit.command.CommandSender;

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

    public abstract void execute(CommandSender sender, String[] args);

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getUsage() {
        return usage;
    }

    public String getPermission() {
        return permission;
    }

}
