package fr.clue.cookieac.process.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientPlayerFlying;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerEntityEffect;
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
public class MovementProcessor extends Processor {

    private FlyingLocation to = new FlyingLocation();
    private FlyingLocation from = new FlyingLocation();
    private FlyingLocation fromFrom = new FlyingLocation();
    private FlyingLocation lastGroundPosition = new FlyingLocation();
    private Material lastTouchedMaterial = Material.DIRT;

    private double deltaX, deltaY, deltaZ, deltaXAbs, deltaZAbs, deltaYAbs, lastDeltaX, lastDeltaY, lastDeltaZ,
            lastDeltaXZ, lastDeltaYaw, lastDeltaPitch, lastDeltaYawAbs, lastDeltaPitchAbs,
            deltaXZ, deltaYaw, deltaPitch, deltaYawAbs, deltaPitchAbs;

    private int tick, offGroundTick, onGroundTick, claimOffGroundTick, claimOnGroundTick;
    private boolean accurateGround, wasAccurateGround, landedFromSlime = true;

    public MovementProcessor(CookiePlayer user) {
        super(user);
    }

    @Override
    public void onPacket(PacketReceiveEvent event) {
        switch (PacketUtil.toPacketReceive(event)) {
            case CLIENT_FLYING:
            case CLIENT_POSITION:
            case CLIENT_LOOK:
            case CLIENT_POSITION_LOOK: {

                WrapperPlayClientPlayerFlying flyingPacket = new WrapperPlayClientPlayerFlying(event);

                double x = flyingPacket.getLocation().getX();
                double y = flyingPacket.getLocation().getY();
                double z = flyingPacket.getLocation().getZ();

                float pitch = flyingPacket.getLocation().getPitch();
                float yaw = flyingPacket.getLocation().getYaw();

                boolean ground = flyingPacket.isOnGround();
                boolean mathGround = CollisionUtils.hasNoSolidBlocksAroundPlayer((Player) event.getPlayer()) && this.getTo().getPosY() % (1 / 64.0d) == 0;
                this.wasAccurateGround = this.accurateGround;
                this.accurateGround = CollisionUtils.accurateGround((Player) event.getPlayer()) ||
                        CollisionUtils.isStandingOnLilypad((Player) event.getPlayer())
                || CollisionUtils.standingOnFence((Player) event.getPlayer());

                if(CollisionUtils.groundMaterial((Player) event.getPlayer()).isSolid()){
                    lastTouchedMaterial = CollisionUtils.groundMaterial((Player) event.getPlayer());
                    landedFromSlime = false;
                }else{
                    if(this.accurateGround){
                        landedFromSlime = true;
                    }
                }
                if(accurateGround){
                    lastGroundPosition.setTick(from.getTick());
                    lastGroundPosition.setWorld(from.getWorld());
                    lastGroundPosition.setPitch(from.getPitch());
                    lastGroundPosition.setYaw(from.getYaw());
                    lastGroundPosition.setPosX(from.getPosX());
                    lastGroundPosition.setPosY(from.getPosY());
                    lastGroundPosition.setPosZ(from.getPosZ());
                    lastGroundPosition.setOnGround(from.isOnGround());
                }
                this.offGroundTick = this.accurateGround ? 0 : offGroundTick + 1;
                this.onGroundTick = this.accurateGround ? onGroundTick + 1 : 0;
                this.claimOffGroundTick = ground ? 0 : claimOffGroundTick + 1;
                this.claimOnGroundTick = ground ? claimOnGroundTick + 1 : 0;

                this.fromFrom.setWorld(this.from.getWorld());
                this.from.setWorld(to.getWorld());
                this.to.setWorld(getData().toBukkit().getWorld());

                this.fromFrom.setOnGround(this.from.isOnGround());
                this.from.setOnGround(this.to.isOnGround());
                this.to.setOnGround(ground);

                this.fromFrom.setTick(this.from.getTick());
                this.from.setTick(this.to.getTick());
                this.to.setTick(this.tick);

                if (flyingPacket.hasPositionChanged()) {

                    this.fromFrom.setPosX(this.from.getPosX());
                    this.fromFrom.setPosY(this.from.getPosY());
                    this.fromFrom.setPosZ(this.from.getPosZ());

                    this.from.setPosX(this.to.getPosX());
                    this.from.setPosY(this.to.getPosY());
                    this.from.setPosZ(this.to.getPosZ());

                    this.to.setPosX(x);
                    this.to.setPosY(y);
                    this.to.setPosZ(z);

                    this.lastDeltaX = this.deltaX;
                    this.lastDeltaY = this.deltaY;
                    this.lastDeltaZ = this.deltaZ;

                    this.deltaY = this.to.getPosY() - this.from.getPosY();
                    this.deltaX = this.to.getPosX() - this.from.getPosX();
                    this.deltaZ = this.to.getPosZ() - this.from.getPosZ();

                    this.deltaXAbs = Math.abs(this.deltaX);
                    this.deltaZAbs = Math.abs(this.deltaZ);
                    this.deltaYAbs = Math.abs(this.deltaY);

                    this.lastDeltaXZ = this.deltaXZ;

                    this.deltaXZ = Math.hypot(this.deltaXAbs, this.deltaZAbs);
                }

                if (flyingPacket.hasRotationChanged()) {

                    this.fromFrom.setYaw(this.from.getYaw());
                    this.fromFrom.setPitch(this.from.getPitch());

                    this.from.setYaw(this.to.getYaw());
                    this.from.setPitch(this.to.getPitch());

                    this.to.setPitch(pitch);
                    this.to.setYaw(yaw);

                    this.lastDeltaYaw = this.deltaYaw;
                    this.lastDeltaPitch = this.deltaPitch;

                    this.deltaYaw = this.to.getYaw() - this.from.getYaw();
                    this.deltaPitch = this.to.getPitch() - this.from.getPitch();

                    this.lastDeltaYawAbs = this.deltaYawAbs;
                    this.lastDeltaPitchAbs = this.deltaPitchAbs;

                    this.deltaYawAbs = Math.abs(this.to.getYaw() - this.from.getYaw());
                    this.deltaPitchAbs = Math.abs(this.to.getPitch() - this.from.getPitch());
                }

                ++this.tick;
                break;
            }
        }
    }
}