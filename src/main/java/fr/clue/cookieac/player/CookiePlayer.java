package fr.clue.cookieac.player;

import com.github.retrooper.packetevents.protocol.world.Location;
import com.viaversion.viaversion.api.Via;
import fr.clue.cookieac.check.Check;
import fr.clue.cookieac.process.ProcessorManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
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
    @Getter
    private PotionData potionData;
    @Getter @Setter
    public Location tpLocation;
    @Getter @Setter
    public long timeJoin;

    public CookiePlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.userName = player.getName();
        this.checks = new ArrayList<>();
        this.processorManager = new ProcessorManager(this);
        this.potionData = new PotionData();
        this.timeJoin = System.currentTimeMillis();
    }

    public Player toBukkit(){
        return player;
    }

    public class PotionData{

        public @NotNull Collection<PotionEffect> getActiveEffects(){
            return player.getActivePotionEffects();
        }

        public int getJumpAmplifier(Player player) {
            for (PotionEffect effect : player.getActivePotionEffects()) {
                if (effect.getType() == PotionEffectType.JUMP) {
                    return effect.getAmplifier();
                }
            }
            return 0;
        }
        public double jumpPower(Player player){
            return getJumpAmplifier(player) > 0 ? 0.1f * (getJumpAmplifier(player) + 1) : 0;
        }
    }

}
