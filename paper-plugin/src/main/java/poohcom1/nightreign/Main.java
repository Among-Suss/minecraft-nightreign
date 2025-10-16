package poohcom1.nightreign;

import org.bukkit.plugin.java.JavaPlugin;
import poohcom1.nightreign.classhelpers.Revenant;

public class Main extends JavaPlugin {
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;

        // Register listeners here
        getServer().getPluginManager().registerEvents(new Revenant(this), this);

        getLogger().info("Nightreign plugin loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Nightreign plugin unloaded!");
    }

    public static Main getInstance() {
        return instance;
    }
}
