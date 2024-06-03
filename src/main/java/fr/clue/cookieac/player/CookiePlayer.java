package fr.clue.cookieac.player;

import com.github.retrooper.packetevents.protocol.player.User;
import fr.clue.cookieac.check.Check;
import fr.clue.cookieac.process.Processor;
import fr.clue.cookieac.process.ProcessorManager;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CookiePlayer {
    // using packetevents i have to finish this lol ok bye gotta go
    private final Player player;
    private final UUID uuid;

    private final String userName;

    @Getter
    private List<Check> checks;

    @Getter
    private ProcessorManager processorManager;

    public CookiePlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.userName = player.getName();
        this.checks = new ArrayList<>();
        this.processorManager = new ProcessorManager(this);
    }

    public Player toBukkit(){
        return player;
    }

}
