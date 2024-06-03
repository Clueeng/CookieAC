package fr.clue.cookieac;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.event.PacketListenerPriority;
import fr.clue.cookieac.check.CheckManager;
import fr.clue.cookieac.event.packet.BukkitListener;
import fr.clue.cookieac.listeners.PacketReceiveListener;
import fr.clue.cookieac.utils.PlayerManager;
import io.github.retrooper.packetevents.factory.spigot.SpigotPacketEventsBuilder;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class CookieAC extends JavaPlugin {
    private static CookieAC COOKIE_INSTANCE;


    // init stuff
    @Getter
    private static PlayerManager playerManager;
    @Getter
    private static CheckManager checkManager;

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
        getLogger().info("Implementing listeners");
        PacketEvents.getAPI().getEventManager().registerListener(new PacketReceiveListener());
        new BukkitListener();


        playerManager = new PlayerManager();
        checkManager = new CheckManager();
        getLogger().info("Loading checks");
        checkManager.loadChecks();

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
