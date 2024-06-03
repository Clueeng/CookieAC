package fr.clue.cookieac.check.impl;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import com.github.retrooper.packetevents.event.PacketSendEvent;
import fr.clue.cookieac.CookieAC;
import fr.clue.cookieac.check.Check;
import fr.clue.cookieac.check.CheckData;
import fr.clue.cookieac.utils.PacketUtil;

@CheckData(name = "Test", experimental = true)
public class TestCheck extends Check {
    @Override
    public void onPacket(PacketReceiveEvent event) {
        getUser().toBukkit().sendMessage("S");
        switch (PacketUtil.toPacketReceive(event)){
            case CLIENT_LOOK:
            case CLIENT_POSITION:
            case CLIENT_POSITION_LOOK:
            case CLIENT_FLYING: {
                //
                double speed = getUser().getProcessorManager().getMovementProcessor().getDeltaXZ();
                fail("LL elon MAH " + getUser().toBukkit().getName() + " : " + speed);
                getUser().toBukkit().sendMessage("S");
                break;
            }
        }
    }
}
