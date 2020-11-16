package me.keshav.mcplugin;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        SpawnMobHandler handler = new SpawnMobHandler(this);
        handler.start();
    }

}
