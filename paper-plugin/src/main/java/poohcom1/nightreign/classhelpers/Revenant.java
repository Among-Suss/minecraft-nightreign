package poohcom1.nightreign.classhelpers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import poohcom1.nightreign.common.Constants;

public class Revenant implements Listener {
    private final Plugin plugin;

    public Revenant(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMobSpawn(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getItem() == null || !event.getItem().getType().name().endsWith("_SPAWN_EGG")) return;

        var isRevenant = event.getPlayer().getScoreboardTags().contains(Constants.TAG_REVENANT);

        if (!isRevenant) {
            return;
        }

        var player = event.getPlayer();
        var playerWorld = player.getWorld();

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        var playerteam = scoreboard.getEntityTeam(player);

        if (playerteam == null) {
            plugin.getLogger().warning("Revenant spawn attempt without team");
            return;
        }

        Entity entity = null;

        switch (event.getItem().getType()) {
            case Material.WITHER_SKELETON_SPAWN_EGG:
                entity = playerWorld.spawnEntity(event.getPlayer().getLocation(), EntityType.WITHER_SKELETON);
                break;
            case Material.WITHER_SPAWN_EGG:
                entity = playerWorld.spawnEntity(event.getPlayer().getLocation(), EntityType.WITHER);
                break;
            case Material.SKELETON_SPAWN_EGG:
                entity = playerWorld.spawnEntity(event.getPlayer().getLocation(), EntityType.SKELETON);
                break;
        }

        if (entity != null) {
            event.setCancelled(true);

            event.setUseItemInHand(Event.Result.ALLOW);

            if (playerteam != null) {
                playerteam.addEntity(entity);
            }
        }
    }
}
