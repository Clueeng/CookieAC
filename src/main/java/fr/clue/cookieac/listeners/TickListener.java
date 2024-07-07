package fr.clue.cookieac.listeners;

import fr.clue.cookieac.CookieAC;
import fr.clue.cookieac.check.Check;
import fr.clue.cookieac.player.CookiePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class TickListener extends BukkitRunnable {
    CookieAC plugin;
    public TickListener(CookieAC plugin) {
        this.plugin = plugin;
}

    @Override
    public void run() {
        for(Player p : Bukkit.getOnlinePlayers()){
            CookiePlayer cp = CookieAC.getPlayerManager().getUser(p);
            for(Check c : cp.getChecks()){
                c.updateExemptionTicks();
            }
        }
    }

    public void start() {
        this.runTaskTimer(plugin, 0L, 1L);
    }
    }