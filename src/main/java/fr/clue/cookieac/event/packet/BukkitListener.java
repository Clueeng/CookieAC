package fr.clue.cookieac.event.packet;
import fr.clue.cookieac.CookieAC;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class BukkitListener implements Listener {
    public BukkitListener() {
        Bukkit.getPluginManager().registerEvents(this, CookieAC.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(final PlayerJoinEvent event) {
        CookieAC.getPlayerManager().addUser(event.getPlayer());
        System.out.println("Added " + event.getPlayer().getName() + " to the list " + CookieAC.getPlayerManager().getUsers());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onLeave(final PlayerQuitEvent event) {
        CookieAC.getPlayerManager().removePlayer(event.getPlayer());
    }

}
