package fr.clue.cookieac.utils;

import fr.clue.cookieac.CookieAC;
import fr.clue.cookieac.player.CookiePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;

public class PunishmentUtils {
    public static void punish(CookiePlayer user, PunishType type, String reason) {
        Player p = user.toBukkit();
        Component prefix = Component.text(CookieAC.cookieName, NamedTextColor.GOLD).append(
                Component.text(" > ",NamedTextColor.YELLOW));
        switch (type){
            case KICK:
                Bukkit.getScheduler().runTask(CookieAC.getInstance(), () -> {
                    Component msg1 = prefix.append(Component.text("You were kicked for " + reason));
                    p.kick(msg1);
                    CookieAC.getPlayerManager().removePlayer(p);
                });
                break;
            case IP_BAN:
                Bukkit.getScheduler().runTask(CookieAC.getInstance(), () -> {
                    Component msg1 = prefix.append(Component.text("You were banned for " + reason));
                    p.kick(msg1);
                    p.banPlayerIP("You were IP banned for " + reason);
                    CookieAC.getPlayerManager().removePlayer(p);
                });
                break;
            case BAN:
                Bukkit.getScheduler().runTask(CookieAC.getInstance(), () -> {
                    Component msg1 = prefix.append(Component.text("You were banned for " + reason));
                    p.kick(msg1);
                    p.ban("You were banned for " + reason, Duration.ofDays(31L), "",true);
                    CookieAC.getPlayerManager().removePlayer(p);
                });
                break;
        }
    }

    public enum PunishType{
        KICK,
        BAN,
        MUTE,
        IP_BAN
    }

}
