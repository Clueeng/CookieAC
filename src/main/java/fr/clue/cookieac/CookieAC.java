package fr.clue.cookieac;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import fr.clue.cookieac.check.Check;
import fr.clue.cookieac.check.CheckManager;
import fr.clue.cookieac.command.EnableAlerts;
import fr.clue.cookieac.event.packet.BukkitListener;
import fr.clue.cookieac.listeners.PacketReceiveListener;
import fr.clue.cookieac.listeners.TickListener;
import fr.clue.cookieac.player.CookiePlayer;
import fr.clue.cookieac.utils.PlayerManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public final class CookieAC extends JavaPlugin {
    private static CookieAC COOKIE_INSTANCE;


    // init stuff
    @Getter
    private static PlayerManager playerManager;
    @Getter
    private static CheckManager checkManager;
    @Getter
    public static String cookieName = "Cookie";

    @Override
    public void onLoad() {
        PacketEvents.setAPI(SpigotPacketEventsBuilder.build(this));
        PacketEvents.getAPI().getSettings().reEncodeByDefault(false).checkForUpdates(true)
                .bStats(true);
        // Load with current settings
        PacketEvents.getAPI().load();
    }

    @Override
    public void onEnable() {
        COOKIE_INSTANCE = this;
        getLogger().info("Implementing listeners AAAAAAAAH TEST");
        PacketEvents.getAPI().getEventManager().registerListener(new PacketReceiveListener());
        new BukkitListener();
        new TickListener(this).start();

        playerManager = new PlayerManager();
        checkManager = new CheckManager();
        getLogger().info("Loading checks");
        checkManager.loadChecks();
        this.getCommand("alerts").setExecutor(new EnableAlerts());
        this.getCommand("alerts").setTabCompleter(new EnableAlerts());

    }

    public static CookieAC getInstance(){
        return COOKIE_INSTANCE;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        PacketEvents.getAPI().terminate();
    }
}
