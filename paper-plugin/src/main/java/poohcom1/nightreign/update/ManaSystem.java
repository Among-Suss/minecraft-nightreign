package poohcom1.nightreign.update;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import poohcom1.nightreign.common.Constants;

public class ManaSystem implements Runnable {
    private final JavaPlugin plugin;

    public ManaSystem(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        var manaScoreboard = getManaScoreboard();
        if (manaScoreboard == null)
            return;

        var players = Bukkit.getOnlinePlayers();
        for (var player : players) {

            var score = manaScoreboard.getScore(player.getName()).getScore();

            var lvl = score / 100;
            var xp = score % 100;
            player.setLevel(lvl);
            player.setExp(xp / 100.0f);
        }
    }

    public double getMana(Player player) {
        var manaScoreboard = getManaScoreboard();
        if (manaScoreboard == null)
            return 0;

        return manaScoreboard.getScore(player).getScore() / 100.0;
    }

    public void setMana(Player player, double mana) {
        var manaScoreboard = getManaScoreboard();
        if (manaScoreboard == null)
            return;

        manaScoreboard.getScore(player).setScore((int)(mana * 100));
    }

    // Util
    private Objective getManaScoreboard() {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        var objective = scoreboard.getObjective(Constants.SCORE_MANA);
        if (objective == null) {
            plugin.getLogger().warning("Cannot find scoreboard: " + Constants.SCORE_MANA);
            return null;
        }

        return objective;
    }
}
