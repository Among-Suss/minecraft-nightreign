package poohcom1.nightreign.classhelpers;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import poohcom1.nightreign.common.Constants;
import poohcom1.nightreign.update.ManaSystem;

import java.util.HashSet;
import java.util.Hashtable;

public class Revenant implements Listener, Runnable {
    private final Plugin plugin;
    private final ManaSystem manaSystem;

    private final HashSet<Entity> summons = new HashSet<>();
    private final Hashtable<Integer, Entity> witherRides = new Hashtable<>(); // wither -> minecart

    public Revenant(Plugin plugin, ManaSystem manaSystem) {
        this.plugin = plugin;
        this.manaSystem = manaSystem;
    }


    @EventHandler
    public void onMobSpawned(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) return;
        if (event.getItem() == null || !event.getItem().getType().name().endsWith("_SPAWN_EGG")) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;

        plugin.getLogger().info("Spawned at: " + event.getInteractionPoint());

        var isRevenant = event.getPlayer().getScoreboardTags().contains(Constants.TAG_REVENANT);

        if (!isRevenant) {
            return;
        }

        var player = event.getPlayer();

        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        var playerteam = scoreboard.getEntityTeam(player);

        if (playerteam == null) {
            plugin.getLogger().warning("Revenant spawn attempt without team");
            return;
        }

        EntityType entityType = EntityType.UNKNOWN;
        Entity entity = null;
        String name = "";
        double manaCost = 0;

        switch (event.getItem().getType()) {
            case Material.SKELETON_SPAWN_EGG:
                manaCost = 3;
                entityType = EntityType.SKELETON;
                name = "Helen";
                break;
            case Material.WITHER_SKELETON_SPAWN_EGG:
                manaCost = 5;
                entityType = EntityType.WITHER_SKELETON;
                name = "Frederick";
                break;
            case Material.WITHER_SPAWN_EGG:
                manaCost = 15;
                entityType = EntityType.WITHER;
                name = "Sebastian";
                break;
        }

        if (entityType == EntityType.UNKNOWN) {
            return;
        }
        if (manaSystem.getMana(player) < manaCost) {
            event.setCancelled(true);
            player.sendMessage("Not enough mana!");
            return;
        }
        entity = player.getWorld().spawnEntity(player.getLocation(), entityType);
        summons.add(entity);

        if (entity.getType() == EntityType.WITHER) {
            var sheep = player.getWorld().spawnEntity(player.getLocation(), EntityType.SHEEP);

            sheep.addPassenger(entity);
            sheep.setInvulnerable(true);
            sheep.setInvisible(true);
            sheep.setSilent(true);
            ((LivingEntity)sheep).setAI(false);

            witherRides.put(entity.getEntityId(), sheep);
            plugin.getLogger().info("Stored wither id: " + entity.getEntityId());
        }

        manaSystem.setMana(player, manaSystem.getMana(player) - manaCost);
        event.setCancelled(true);
        playerteam.addEntity(entity);
        entity.setCustomName(player.getName() + "'s " + name);
        entity.setCustomNameVisible(true);
    }

    @Override
    public void run() {
        HashSet<Entity> invalidEntities = new HashSet<>();

        plugin.getLogger().info("Summons count: " + summons.size());

        for (var e : summons) {
            if (!e.isValid() || !e.isInWorld()) {
                invalidEntities.add(e);
            }

            if (!(e instanceof Damageable damageable) || !(e instanceof Attributable attr)) {
                continue;
            }
            var health = damageable.getHealth();
            var resultHealth = Math.max(0, health - attr.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() / 20);

            Bukkit.getScheduler().runTask(plugin, () -> {
                damageable.setHealth(resultHealth);
                if (resultHealth <= 0.0) {
                    invalidEntities.add(e);
                }
            });
        }

        for (var e : invalidEntities) {
            if (witherRides.containsKey(e.getEntityId())) {
                var minecart = witherRides.get(e.getEntityId());
                witherRides.remove(e.getEntityId());
                Bukkit.getScheduler().runTask(plugin, minecart::remove);
            }

            summons.remove(e);
        }
    }

}
