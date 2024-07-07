package fr.clue.cookieac.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class CombatUtil {
    public static Player getPlayer(int entityID) {
        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
            if (p.getEntityId() == entityID) return p;
        }
        return null;
    }
}
