package fr.clue.cookieac.check.impl.gravity;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import fr.clue.cookieac.check.Check;
import fr.clue.cookieac.check.CheckData;
import fr.clue.cookieac.utils.CollisionUtils;
import fr.clue.cookieac.utils.PacketUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;

import java.nio.file.WatchService;

@CheckData(name = "Gravity", type = "A", description = "Check if the player is following the MCP's default gravity")
public class GravityA extends Check {
    @Override
    public void onPacket(PacketReceiveEvent event) {
        switch (PacketUtil.toPacketReceive(event)){
            case CLIENT_LOOK:
            case CLIENT_POSITION:
            case CLIENT_POSITION_LOOK:
            case CLIENT_FLYING: {
                boolean wasOnSlime = getUser().getProcessorManager().getMovementProcessor().getLastTouchedMaterial().equals(Material.SLIME_BLOCK);
                // exempts slime blocks and fly mode and liquid
                if((getUser().toBukkit().isFlying() && getUser().toBukkit().getAllowFlight())){ exemptionTicks = 10;return; }
                if(wasOnSlime){ exemptionTicks = 3;return; }
                if(getUser().toBukkit().isInWater() || getUser().toBukkit().isUnderWater() || getUser().toBukkit().isInLava()) { exemptionTicks = 7; return;}
                if(CollisionUtils.isSolidBlockAbovePlayer(getUser().toBukkit(),0.2, true)){
                    exemptionTicks = 4;
                    return;
                }

                int offGroundTick = getUser().getProcessorManager().getMovementProcessor().getOffGroundTick();
                double deltaY = getUser().getProcessorManager().getMovementProcessor().getDeltaY();
                boolean nearGround = CollisionUtils.nearGround(getUser().toBukkit());
                double groundDist = CollisionUtils.calculateDistanceToGround(getUser().toBukkit());
                double ceilingDist = CollisionUtils.calculateDistanceToCeiling(getUser().toBukkit());
                boolean nearSlime = CollisionUtils.calculateDistanceToSlimeBlock(getUser().toBukkit()) < 0.25 + Math.max(0, deltaY);
                boolean offGround = offGroundTick > 3;
                //getUser().toBukkit().sendMessage("Lol is that updating " + getUser().toBukkit().getTicksLived() + "ground state: " + !offGround + " off ground for : " + offGroundTick);
                if((offGround && groundDist < 0.5 || (offGroundTick < 10 && groundDist < 0.9 && offGroundTick > 5)) && deltaY < 0.01){
                    exemptionTicks = 2;
                    return;
                }
                //getUser().toBukkit().sendMessage("ceil : " + ceilingDist);
                if(deltaY > 0.41 && deltaY < 0.42 && predictedDeltaY() > -0.304 && predictedDeltaY() < -0.305 && offGroundTick > 9 && offGroundTick < 12 && groundDist > 0.9 && groundDist < 1.6
                        || ceilingDist <= 0.5){
                    exemptionTicks = 2;
                    return;
                }
                if(nearSlime){
                    exemptionTicks = 1;
                    return;
                }

                double difference = Math.abs(
                        deltaY - predictedDeltaY()
                );

                if(difference > 0.003 && offGround && (deltaY < -0.2 || groundDist > 0.5)){
                    fail(String.valueOf("dY: " + deltaY + " pD: " + predictedDeltaY() + " dif: " + difference + " og: " + offGroundTick + " gd: " + groundDist));
                }else{
                    violations = Math.max(0, violations - 0.0025f);
                }
                break;
            }
        }
    }

    public double predictedDeltaY(){
        double lastDeltaY = getUser().getProcessorManager().getMovementProcessor().getLastDeltaY();
        double gravity = 0.9800000190734863d;
        double fall = 0.08d;
        double pred = (lastDeltaY - fall) * gravity;
        if(getUser().getProcessorManager().getMovementProcessor().getDeltaY() > 0.0 &&
                !getUser().getProcessorManager().getMovementProcessor().getFrom().isOnGround() &&
                getUser().getProcessorManager().getMovementProcessor().isAccurateGround())
            pred = 0.42f + (getUser().getPotionData().getJumpAmplifier(getUser().toBukkit()));
        return pred;
    }


}
