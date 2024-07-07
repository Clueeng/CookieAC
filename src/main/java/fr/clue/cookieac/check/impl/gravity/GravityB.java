package fr.clue.cookieac.check.impl.gravity;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import fr.clue.cookieac.check.Check;
import fr.clue.cookieac.check.CheckData;
import fr.clue.cookieac.utils.CollisionUtils;
import fr.clue.cookieac.utils.PacketUtil;
import org.bukkit.Material;

@CheckData(name = "Gravity", type = "B", description = "Too high/low motion", experimental = true, punishmentVL = 10)
public class GravityB extends Check {


    @Override
    public void onPacket(PacketReceiveEvent event) {
        // spamming jump on stairs will cause a false flag with a motionY of 0.5
        switch (PacketUtil.toPacketReceive(event)) {
            case CLIENT_LOOK:
            case CLIENT_POSITION:
            case CLIENT_POSITION_LOOK:
            case CLIENT_FLYING: {
                double deltaY = getUser().getProcessorManager().getMovementProcessor().getDeltaY();
                boolean wasOnGround = getUser().getProcessorManager().getMovementProcessor().isWasAccurateGround();
                boolean onGround = getUser().getProcessorManager().getMovementProcessor().isAccurateGround();
                if(!onGround && wasOnGround){
                    // just jumped, motiony should be 0.42 logically
                    if(deltaY > maxY()){
                        fail("dY: " + deltaY + " mY: " + maxY() + " (jumped from the ground too high)");
                    }
                }
            }
        }
    }


    public double maxY(){
        boolean onStairs = CollisionUtils.onStairs(getUser().toBukkit());
        boolean wasOnSlime = getUser().getProcessorManager().getMovementProcessor().getLastTouchedMaterial().equals(Material.SLIME_BLOCK);
        double d = 0.42d;
        if(wasOnSlime){
            d = 10;
        }
        if(onStairs){
            d += 0.09;
        }
        d += getUser().getPotionData().jumpPower(getUser().toBukkit());
        return d;
    }
}
