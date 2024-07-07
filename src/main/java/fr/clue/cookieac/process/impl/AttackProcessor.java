package fr.clue.cookieac.process.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientEntityAction;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerAttachEntity;
import fr.clue.cookieac.player.CookiePlayer;
import fr.clue.cookieac.process.Processor;
import fr.clue.cookieac.process.ProcessorInfo;
import fr.clue.cookieac.utils.CollisionUtils;
import fr.clue.cookieac.utils.PacketUtil;
import fr.clue.cookieac.utils.location.FlyingLocation;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.entity.Player;

@Getter
@Setter
@ProcessorInfo(name = "Movement")
public class AttackProcessor extends Processor {

    private FlyingLocation lastAttackPosition = new FlyingLocation();
    private Material lastTouchedMaterial = Material.DIRT;

    private double deltaX, deltaY, deltaZ, deltaXAbs, deltaZAbs, deltaYAbs, lastDeltaX, lastDeltaY, lastDeltaZ,
            lastDeltaXZ, lastDeltaYaw, lastDeltaPitch, lastDeltaYawAbs, lastDeltaPitchAbs,
            deltaXZ, deltaYaw, deltaPitch, deltaYawAbs, deltaPitchAbs;

    private int tickSinceLastDamage;
    private boolean attacked;

    public AttackProcessor(CookiePlayer user) {
        super(user);
    }

    @Override
    public void onPacket(PacketSendEvent event) {
        attacked = false;
        tickSinceLastDamage++;
        switch (PacketUtil.toPacketSend(event)) {
            case DAMAGE_PLAYER:{
                tickSinceLastDamage = 0;
                attacked = true;
                break;
            }
        }
    }
}