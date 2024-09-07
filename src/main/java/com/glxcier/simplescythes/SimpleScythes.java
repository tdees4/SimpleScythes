package com.glxcier.simplescythes;

import com.glxcier.simplescythes.commands.ScytheCommand;
import com.glxcier.simplescythes.listeners.BlockListener;
import com.glxcier.simplescythes.listeners.EnchantingListener;
import com.glxcier.simplescythes.listeners.RepairListener;
import com.glxcier.simplescythes.tools.ScytheHandler;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class SimpleScythes extends JavaPlugin {

    public static Plugin instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        saveDefaultConfig();

        instance = this;
        ScytheHandler scytheHandler = new ScytheHandler();

        getServer().getPluginManager().registerEvents(new BlockListener(this, scytheHandler), this);
        getServer().getPluginManager().registerEvents(new SmithingListener(scytheHandler), this);
        getServer().getPluginManager().registerEvents(new EnchantingListener(scytheHandler), this);
        getServer().getPluginManager().registerEvents(new RepairListener(scytheHandler), this);
        this.getCommand("scythe").setExecutor(new ScytheCommand(scytheHandler));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
