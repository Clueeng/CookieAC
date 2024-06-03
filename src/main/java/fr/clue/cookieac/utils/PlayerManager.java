package fr.clue.cookieac.utils;

import com.github.retrooper.packetevents.PacketEvents;
import com.github.retrooper.packetevents.protocol.player.User;
import fr.clue.cookieac.CookieAC;
import fr.clue.cookieac.player.CookiePlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class PlayerManager {
    private final Map<UUID, CookiePlayer> userMap = new ConcurrentHashMap<>();

    public void addUser(Player player) {
        CookiePlayer user = new CookiePlayer(player);

        this.userMap.put(player.getUniqueId(), user);
        CookieAC.getCheckManager().loadToPlayer(user);
    }

    public CookiePlayer getUser(Player player) {
        return this.userMap.get(player.getUniqueId());
    }

    public void removePlayer(Player player) {
        this.userMap.remove(player.getUniqueId());
    }

    public CookiePlayer getPlayer(UUID uuid) {
        return this.userMap.get(uuid);
    }
    public ArrayList<CookiePlayer> getUsers(){
        return new ArrayList<>(userMap.values());
    }
}