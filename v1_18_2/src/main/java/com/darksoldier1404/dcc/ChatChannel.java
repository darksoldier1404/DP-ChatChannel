package com.darksoldier1404.dcc;

import com.darksoldier1404.dcc.commands.DCCCommand;
import com.darksoldier1404.dcc.events.DCCEvent;
import com.darksoldier1404.dppc.utils.DataContainer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class ChatChannel extends JavaPlugin {
    private static ChatChannel plugin;
    public static DataContainer data;
    public static boolean force;

    public static ChatChannel getInstance() {
        return plugin;
    }

    public void onEnable() {
        plugin = this;
        data = new DataContainer(plugin);
        force = data.getConfig().getBoolean("Settings.forceUseChannel");
        plugin.getServer().getPluginManager().registerEvents(new DCCEvent(), plugin);
        Objects.requireNonNull(getCommand("dcc")).setExecutor(new DCCCommand());
    }

    public void onDisable() {
        Bukkit.getOnlinePlayers().forEach(player -> data.saveAndLeave(player.getUniqueId()));
        data.save();
    }
}
