package fr.clue.cookieac.utils;

import fr.clue.cookieac.CookieAC;
import fr.clue.cookieac.player.CookiePlayer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;

import java.time.Duration;

public class PunishmentUtils {
    public static void punish(CookiePlayer user, PunishType type, String reason) {
        Component prefix = Component.text(CookieAC.cookieName, NamedTextColor.GOLD).append(
                Component.text(" > ",NamedTextColor.YELLOW));
        switch (type){
            case KICK:
                Bukkit.getScheduler().runTask(CookieAC.getInstance(), () -> {
                    Component msg1 = prefix.append(Component.text("You were kicked for " + reason));
                    user.toBukkit().kick(msg1);
                });
                break;
            case IP_BAN:
                Bukkit.getScheduler().runTask(CookieAC.getInstance(), () -> {
                    Component msg1 = prefix.append(Component.text("You were banned for " + reason));
                    user.toBukkit().kick(msg1);
                    user.toBukkit().banPlayerIP("You were IP banned for " + reason);
                });
                break;
            case BAN:
                Bukkit.getScheduler().runTask(CookieAC.getInstance(), () -> {
                    Component msg1 = prefix.append(Component.text("You were banned for " + reason));
                    user.toBukkit().kick(msg1);
                    user.toBukkit().ban("You were banned for " + reason, Duration.ofDays(31L), "",true);
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
