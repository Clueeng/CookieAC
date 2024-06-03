package fr.clue.cookieac.listeners;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.protocol.player.User;
import fr.clue.cookieac.CookieAC;
import fr.clue.cookieac.player.CookiePlayer;
import fr.clue.cookieac.utils.PacketUtil;
import org.bukkit.entity.Player;

public class PacketReceiveListener extends PacketListenerAbstract {
    //Clientbound
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        PacketUtil.toPacketReceive(event);

        Player player = (Player) event.getPlayer();
        player.sendMessage("Debug 1, PacketReceiveListener#onPacketReceive");
        if (player == null) {
            return;
        }

        CookiePlayer user = CookieAC.getPlayerManager().getUser(player);

        if (user == null) {
            return;
        }

        //Register clientbound packets for checks
        user.getChecks().forEach(check -> {
            user.toBukkit().sendMessage("Debug 2, loop through checks, and use onPacket");
            check.onPacket(event);
        });

        //run client processors
        user.getProcessorManager().getProcessors().forEach(processor -> processor.onPacket(event));
    }


    //Serverbound
    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketUtil.toPacketSend(event);

        Player player = (Player) event.getPlayer();

        if (player == null) {
            return;
        }

        CookiePlayer user = CookieAC.getPlayerManager().getUser(player);

        if (user == null) {
            return;
        }

        //Register serverbound packets for checks
        user.getChecks().forEach(check -> {
            check.onPacket(event);
        });

        //run server processors
        user.getProcessorManager().getProcessors().forEach(processor -> processor.onPacket(event));
    }

}
