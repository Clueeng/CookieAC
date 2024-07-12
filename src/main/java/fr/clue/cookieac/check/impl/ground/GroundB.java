package fr.clue.cookieac.check.impl.ground;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import fr.clue.cookieac.check.Check;
import fr.clue.cookieac.check.CheckData;
import fr.clue.cookieac.utils.CollisionUtils;
import fr.clue.cookieac.utils.PacketUtil;

@CheckData(name = "Ground", type = "B", punishmentVL = 15.0f, description = "Prevents 'no ground' spoof from being performed")
public class GroundB extends Check {

    private final double moduloGround = 1/64d;
    @Override
    public void onPacket(PacketReceiveEvent event) {
        switch (PacketUtil.toPacketReceive(event)){
            case CLIENT_LOOK:
            case CLIENT_POSITION:
            case CLIENT_POSITION_LOOK:
            case CLIENT_FLYING: {
                int offGroundTick = getUser().getProcessorManager().getMovementProcessor().getOffGroundTick();
                int onGroundTick = getUser().getProcessorManager().getMovementProcessor().getOnGroundTick();
                int claimOffGroundTick = getUser().getProcessorManager().getMovementProcessor().getClaimOffGroundTick();
                int claimOnGroundTick = getUser().getProcessorManager().getMovementProcessor().getClaimOnGroundTick();
                boolean claimGround = getUser().getProcessorManager().getMovementProcessor().getTo().isOnGround();
                boolean serverGround = getUser().getProcessorManager().getMovementProcessor().isAccurateGround();
                boolean nearGround = CollisionUtils.nearGround(getUser().toBukkit());
                // check if the player is not near the server ground (1 block maybe)
                // We have to check if the player is off the ground for more than 1 tick to prevent some falses due to imperfect ground check

                // The player will be loading chunks and will be off ground client side but on ground server side, this will flag for ground B
                // We need to take that into account
                // We could also check for the motion Y value of the player when he is in the void of unloaded chunks
                // this value being -0.09800000190734863f
                exemptOnJoin();

                // Ground B can false if the player is inside blocks, due to my very good ground method, we need to exempt that (free disabler)


                if(claimOffGroundTick > 2 && serverGround && nearGround && onGroundTick > 5){
                    fail(event,"s:" + serverGround + " c: " + claimGround + " t: " + offGroundTick);
                }else{
                    violations = Math.max(0, violations - 0.0025f);
                }
                break;
            }
        }
    }
}
