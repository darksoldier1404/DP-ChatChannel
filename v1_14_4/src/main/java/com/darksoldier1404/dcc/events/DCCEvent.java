package com.darksoldier1404.dcc.events;

import com.darksoldier1404.dcc.ChatChannel;
import com.darksoldier1404.dcc.functions.DCCFunction;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@SuppressWarnings("all")
public class DCCEvent implements Listener {
    private final ChatChannel plugin = ChatChannel.getInstance();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        plugin.data.initUserData(e.getPlayer().getUniqueId());
        YamlConfiguration data = plugin.data.getUserData(e.getPlayer().getUniqueId());
        if (data.get("Enabled") == null) {
            data.set("Enabled", false);
        }
        if (data.get("Channel") == null) {
            data.set("Channel", "1");
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        plugin.data.saveAndLeave(e.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onChat(PlayerChatEvent e) {
        if (plugin.data.getUserData(e.getPlayer().getUniqueId()).getBoolean("Enabled")) {
            e.setCancelled(true);
            DCCFunction.sendMessage(e.getPlayer(), e.getMessage());
        } else {
            if (plugin.force) {
                e.setCancelled(true);
                DCCFunction.sendMessage(e.getPlayer(), e.getMessage(), "1");
            }else{
                try{
                    for(Player p : e.getRecipients()) {
                        if (plugin.data.getUserData(p.getUniqueId()).getBoolean("Enabled")) {
                            e.getRecipients().remove(p);
                        }
                    }
                }catch (Exception ignored){

                }
            }
        }
    }
}
