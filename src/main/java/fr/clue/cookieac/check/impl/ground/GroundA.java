package fr.clue.cookieac.check.impl.ground;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import fr.clue.cookieac.check.Check;
import fr.clue.cookieac.check.CheckData;
import fr.clue.cookieac.utils.CollisionUtils;
import fr.clue.cookieac.utils.PacketUtil;
import net.kyori.adventure.text.Component;

@CheckData(name = "Ground", type = "A", punishmentVL = 15.0f, description = "Prevents ground spoof from being performed")
public class GroundA extends Check {

    private final double moduloGround = 1/64d;
    @Override
    public void onPacket(PacketReceiveEvent event) {
        switch (PacketUtil.toPacketReceive(event)){
            case CLIENT_LOOK:
            case CLIENT_POSITION:
            case CLIENT_POSITION_LOOK:
            case CLIENT_FLYING: {
                int offGroundTick = getUser().getProcessorManager().getMovementProcessor().getOffGroundTick();
                int claimOffGroundTick = getUser().getProcessorManager().getMovementProcessor().getClaimOffGroundTick();
                int claimOnGroundTick = getUser().getProcessorManager().getMovementProcessor().getClaimOnGroundTick();
                boolean claimGround = getUser().getProcessorManager().getMovementProcessor().getTo().isOnGround();
                boolean serverGround = getUser().getProcessorManager().getMovementProcessor().isAccurateGround();
                boolean nearGround = CollisionUtils.nearGround(getUser().toBukkit());
                // check if the player is not near the server ground (1 block maybe)
                // We have to check if the player is off the ground for more than 1 tick to prevent some falses due to imperfect ground check
                //getUser().toBukkit().sendMessage(Component.text("Server ground : " + serverGround));
                // just checking if plugin is built correctly lol
                if(claimOnGroundTick > 2 && !serverGround && !nearGround && offGroundTick > 5){
                    fail("s:" + serverGround + " c: " + claimGround + " t: " + offGroundTick);
                }else{
                    violations = Math.max(0, violations - 0.0025f);
                }
                break;
            }
        }
    }
}
