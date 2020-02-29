package me.border.arenatimeout;

import me.border.arenatimeout.commands.CommandArenaTimeout;
import me.border.arenatimeout.listeners.TimeoutListener;
import me.border.arenatimeout.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.HashMap;

public class Main extends JavaPlugin {

    public static HashMap<String, Long> timeouted = new HashMap<String, Long>();
    public static String Path = "plugins/ArenaTimeout" + File.separator + "TimeoutList.dat";

    public void onEnable(){
        getConfig().options().copyDefaults(true);
        new CommandArenaTimeout(this);
        new Utils(this);
        getServer().getPluginManager().registerEvents(new TimeoutListener(this), this);

        File file = new File(Path);
        new File("plugins/TempBan").mkdir();

        if(file.exists()){
            timeouted = load();
        }

        if(timeouted == null){
            timeouted = new HashMap<String, Long>();
        }
    }

    public void onDisable(){
        save();
    }

    public static void save(){
        File file = new File("plugins/ArenaTimeout" + File.separator + "TimeoutList.dat");
        new File("plugins/ArenaTimeout").mkdir();
        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(Path));
            oos.writeObject(timeouted);
            oos.flush();
            oos.close();
            //Handle I/O exceptions
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    @SuppressWarnings("unchecked")
    public static HashMap<String, Long> load(){
        try{
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream(Path));
            Object result = ois.readObject();
            ois.close();
            return (HashMap<String,Long>)result;
        }catch(Exception e){
            return null;
        }
    }
}
