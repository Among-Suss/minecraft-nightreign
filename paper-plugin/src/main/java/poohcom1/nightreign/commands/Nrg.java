package poohcom1.nightreign.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.jetbrains.annotations.NotNull;
import poohcom1.nightreign.common.Constants;

public class Nrg implements CommandExecutor {
    private JavaPlugin plugin;

    public Nrg(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (!(sender.isOp())) {
            return true;
        }

        if (args.length == 0) {
            sender.sendMessage("nrg executed");
            return true;
        }

        switch (args[0]) {
            case "update-mana": {
                if (!(sender instanceof Player player))
                    return true;

                try {
                    Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                    var objective = scoreboard.getObjective(Constants.SCORE_MANA);
                    if (objective == null) {
                        plugin.getLogger().warning("Cannot find scoreboard: " + Constants.SCORE_MANA);
                        return true;
                    }
                    var score = objective.getScore(player.getName()).getScore();

                    var lvl = score / 100;
                    var xp = score % 100;
                    player.setLevel(lvl);
                    player.setExp(xp / 100.0f);
                } catch (Exception e) {
                    sender.sendMessage("Failed to run /nrg update-mana: " + e);
                    plugin.getLogger().warning(e.toString());

                }
                break;
            }
            case "xp":
                if (!(sender instanceof Player player))
                    return true;

                try {
                    int level = Integer.parseInt(args[1]);
                    int xp = Integer.parseInt(args[2]);

                    player.setLevel(level);
                    player.setExp(xp);
                } catch (Exception e) {
                    sender.sendMessage("Failed to run /nrg xp: " + e);
                }

                break;
        }

        return true;
    }
}
