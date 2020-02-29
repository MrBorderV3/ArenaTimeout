package me.border.arenatimeout.commands;

import me.border.arenatimeout.Main;
import me.border.arenatimeout.utils.TimeoutUnit;
import me.border.arenatimeout.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.HashMap;

public class CommandArenaTimeout implements CommandExecutor {

    public Main plugin;

    public CommandArenaTimeout(Main plugin){
        this.plugin = plugin;

        plugin.getCommand("timeout").setExecutor(this);
    }

    public boolean onCommand(final CommandSender sender,final Command cmd,final String label,final String[] args) {

        if (sender.hasPermission("mysticempire.timeout")){
            if (args.length <= 1){
                sender.sendMessage(Utils.ucs("Timeout.correct_usage"));
                return true;
            }
            final OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(args[0]);
            if (args[1].equalsIgnoreCase("off")){
                if(getTimeout().containsKey(offlineTarget.getName().toLowerCase())){
                    getTimeout().remove(offlineTarget.getName().toLowerCase());
                    for (final Player all : Bukkit.getOnlinePlayers()) {
                        if (all.hasPermission("mysticempire.timeout")) {
                            all.sendMessage(Utils.ucs("Timeout.unblacklisted").replaceAll("%player%", sender.getName()).replaceAll("%target%", offlineTarget.getName()));
                        }
                    }
                    if (offlineTarget.isOnline()) {
                        offlineTarget.getPlayer().sendMessage(Utils.ucs("Timeout.notification_cancel"));
                    }
                    return true;
                }else{
                    sender.sendMessage(Utils.ucs("Timeout.playerNotBlacklisted").replaceAll("%target%", offlineTarget.getName()));
                    return true;
                }
            }
            try
            {
                final String unit = args[1].substring(args[1].length() - 1);
                final int temp = Integer.parseInt(args[1].substring(0, args[1].length() - 1));
                long endOfTimeout = System.currentTimeMillis() + TimeoutUnit.getTicks(unit, temp);

                long now = System.currentTimeMillis();
                long diff = endOfTimeout - now;
                if (diff > 0) {
                    getTimeout().remove(offlineTarget.getName().toLowerCase());
                    setTimeout(offlineTarget.getName().toLowerCase(), endOfTimeout);

                    args[0] = "";
                    args[1] = "";
                    String message = getMSG(endOfTimeout);
                    StringBuilder reason = new StringBuilder();
                    for (String s : args) {
                        reason.append(s);
                    }
                    for (final Player all : Bukkit.getServer().getOnlinePlayers()) {
                        if (all.hasPermission("mysticempire.timeout")) {
                            all.sendMessage(Utils.ucs("Timeout.notification").replaceAll("%player%", sender.getName()).replaceAll("%target%", offlineTarget.getName()).replaceAll("%time%", message).replaceAll("%reason%", String.valueOf(reason)));
                        }
                    }
                    if (offlineTarget.isOnline()) {
                        offlineTarget.getPlayer().sendMessage(Utils.ucs("Timeout.blacklisted").replaceAll("%time%", message));
                        String world = offlineTarget.getPlayer().getLocation().getWorld().getName();
                        if (world.equalsIgnoreCase(Utils.cs("Timeout.world_name")) || world.equalsIgnoreCase(Utils.cs("Timeout.world_two_name")) ||
                                world.equalsIgnoreCase("Timeout.world_three_name")){
                            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + offlineTarget.getName());
                        }
                    }
                    return true;
                } else {
                    sender.sendMessage(Utils.ucs("Timeout.correct_usage"));
                    return true;
                }
            } catch (NumberFormatException e){
                sender.sendMessage(Utils.ucs("Timeout.correct_usage"));
                return true;
            }
        } else {
            sender.sendMessage(Utils.ucs("noPermission"));
        }

        return false;
    }


    public HashMap<String, Long> getTimeout(){
        return Main.timeouted;
    }

    public void setTimeout(String name, long end){
        getTimeout().put(name, end);
    }

    public static String getMSG(long endOfBan){
        String message = "";

        long now = System.currentTimeMillis();
        long diff = endOfBan - now;
        int seconds = (int) (diff / 1000);

        if(seconds >= 60*60*24){
            int days = seconds / (60*60*24);
            seconds = seconds % (60*60*24);

            message += days + " Day(s) ";
        }
        if(seconds >= 60*60){
            int hours = seconds / (60*60);
            seconds = seconds % (60*60);

            message += hours + " Hour(s) ";
        }
        if(seconds >= 60){
            int min = seconds / 60;
            seconds = seconds % 60;

            message += min + " Minute(s) ";
        }
        if(seconds >= 0){
            message += seconds + " Second(s) ";
        }

        return message;
    }
}

