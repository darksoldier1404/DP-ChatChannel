package com.darksoldier1404.dcc.functions;

import com.darksoldier1404.dcc.ChatChannel;
import com.darksoldier1404.dppc.utils.ColorUtils;
import com.darksoldier1404.dppc.utils.DataContainer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

@SuppressWarnings("all")
public class DCCFunction {
    private static final ChatChannel plugin = ChatChannel.getInstance();
    private static final DataContainer data = plugin.data;

    public static void force(Player p) {
        if (data.getConfig().getBoolean("Settings.forceUseChannel")) {
            data.getConfig().set("Settings.forceUseChannel", false);
            plugin.force = false;
            p.sendMessage(data.getPrefix() + "채팅 채널을 강제로 사용하지 않도록 만들었습니다.");
        }else{
            data.getConfig().set("Settings.forceUseChannel", true);
            plugin.force = true;
            p.sendMessage(data.getPrefix() + "채팅 채널을 강제로 사용하도록 만들었습니다.");
        }
        data.save();
    }

    public static void setChannel(Player p, String channel) {
        try{
            int channelNum = Integer.parseInt(channel);
            data.getUserData(p.getUniqueId()).set("Channel", channelNum);
            data.getUserData(p.getUniqueId()).set("Enabled", true);
            p.sendMessage(data.getPrefix() + "채팅 채널을 " + channel + "로 변경하였습니다.");
        }catch (Exception e){
            p.sendMessage(data.getPrefix() + "채널은 숫자만 입력해주세요.");
            return;
        }
    }

    public static void setDefault(Player p) {
        if(plugin.force) {
            p.sendMessage(data.getPrefix() + "전체 채팅으로 변경할 수 없습니다.");
            return;
        }
        data.getUserData(p.getUniqueId()).set("Enabled", false);
        p.sendMessage(data.getPrefix() + "채팅 채널을 전체 채팅으로 변경하였습니다.");
    }

    public static String getCurrentChannel(Player p) {
        return data.getUserData(p.getUniqueId()).getString("Channel") == null ? " " : data.getUserData(p.getUniqueId()).getString("Channel");
    }

    public static boolean isEnabled(Player p) {
        return data.getUserData(p.getUniqueId()).getBoolean("Enabled");
    }

    public static void sendMessage(Player p, String message) {
        String cch = getCurrentChannel(p);
        String msg = ColorUtils.applyColor(data.getConfig().getString("Settings.channelPrefix").replace("<channel>", cch).replace("<player>", p.getName())).replace("<message>", message);
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(getCurrentChannel(player).equals(cch)) {
                if(isEnabled(player)) {
                    player.sendMessage(msg);
                }
            }
        });
        plugin.getLogger().info(msg);
    }
    public static void sendMessage(Player p, String message, String channel) {
        String msg = ColorUtils.applyColor(data.getConfig().getString("Settings.channelPrefix").replace("<channel>", channel).replace("<player>", p.getName())).replace("<message>", message);
        Bukkit.getOnlinePlayers().forEach(player -> {
            if(getCurrentChannel(player).equals(channel)) {
                if(isEnabled(player)) {
                    player.sendMessage(msg);
                }
            }
        });
        plugin.getLogger().info(msg);
    }
}
