package poohcom1.nightreign;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import poohcom1.nightreign.classhelpers.Revenant;
import poohcom1.nightreign.commands.Nrg;
import poohcom1.nightreign.update.ManaSystem;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        // Register listeners here
        var manaSystem = new ManaSystem(this);
        var revenant = new Revenant(this, manaSystem);

        getServer().getPluginManager().registerEvents(revenant, this);
        getCommand("nrg").setExecutor(new Nrg(this));

        Bukkit.getScheduler().runTaskTimerAsynchronously(this, manaSystem, 0L, 2L);
        Bukkit.getScheduler().runTaskTimerAsynchronously(this, revenant, 0L, 20L);

        getLogger().info("Nightreign plugin loaded!");
    }

    @Override
    public void onDisable() {
        getLogger().info("Nightreign plugin unloaded!");
    }
}
