package me.border.arenatimeout.listeners;

import me.border.arenatimeout.Main;
import me.border.arenatimeout.commands.CommandArenaTimeout;
import me.border.arenatimeout.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;

public class TimeoutListener implements Listener {
    public Main plugin;
    public TimeoutListener(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player p = e.getPlayer();

        if(getTimeout().containsKey(p.getName().toLowerCase())){
            if(getTimeout().get(p.getName().toLowerCase()) != null){
                long endOfBan = getTimeout().get(p.getName().toLowerCase());
                long now = System.currentTimeMillis();
                long diff = endOfBan - now;

                if(diff<=0){
                    getTimeout().remove(p.getName().toLowerCase());
                }else{
                    if (p.getWorld().getName().equals(Utils.cs("Timeout.world_name")) || p.getWorld().getName().equals(Utils.cs("Timeout.world_two_name")) ||
                            p.getWorld().getName().equals(Utils.cs("Timeout.world_three_name")) ){
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + p.getName());
                        p.sendMessage(Utils.ucs("Timeout.attempt_teleport").replaceAll("%time%", CommandArenaTimeout.getMSG(endOfBan)));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        Player p = e.getPlayer();

        if(getTimeout().containsKey(p.getName().toLowerCase())){
            if(getTimeout().get(p.getName().toLowerCase()) != null){
                long endOfBan = getTimeout().get(p.getName().toLowerCase());
                long now = System.currentTimeMillis();
                long diff = endOfBan - now;

                if(diff<=0){
                    getTimeout().remove(p.getName().toLowerCase());
                }else{
                    if (e.getTo().getWorld().getName().equals(Utils.cs("Timeout.world_name")) || e.getTo().getWorld().getName().equals(Utils.cs("Timeout.world_two_name")) ||
                            e.getTo().getWorld().getName().equals(Utils.cs("Timeout.world_three_name"))){
                        e.setCancelled(true);
                        p.sendMessage(Utils.ucs("Timeout.attempt_teleport").replaceAll("%time%", CommandArenaTimeout.getMSG(endOfBan)));
                    }
                }
            }
        }
    }

    public HashMap<String, Long> getTimeout(){
        return Main.timeouted;
    }

}
