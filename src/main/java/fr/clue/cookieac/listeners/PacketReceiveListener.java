package fr.clue.cookieac.listeners;

import com.github.retrooper.packetevents.event.*;
import com.github.retrooper.packetevents.wrapper.play.client.WrapperPlayClientInteractEntity;
import com.github.retrooper.packetevents.wrapper.play.server.WrapperPlayServerPlayerPositionAndLook;
import fr.clue.cookieac.CookieAC;
import fr.clue.cookieac.player.CookiePlayer;
import fr.clue.cookieac.utils.CombatUtil;
import fr.clue.cookieac.utils.PacketUtil;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PacketReceiveListener extends PacketListenerAbstract {
    //Clientbound
    @Override
    public void onPacketReceive(PacketReceiveEvent event) {

        PacketUtil.Packets p = PacketUtil.toPacketReceive(event);

        Player player = (Player) event.getPlayer();
        if (player == null) {
            return;
        }

        CookiePlayer user = CookieAC.getPlayerManager().getUser(player);

        if (user == null) {
            return;
        }

        if(p.equals(PacketUtil.Packets.CLIENT_USE_ENTITY)){
            WrapperPlayClientInteractEntity w = new WrapperPlayClientInteractEntity(event);
            Player victim = CombatUtil.getPlayer(w.getEntityId());

            assert victim != null;
            CookiePlayer victimPlayer = CookieAC.getPlayerManager().getUser(victim);
            victimPlayer.getChecks().forEach(check -> {
                check.exemptionTicks = 4; // 1000 until client accepts idk
            });
        }

        if(p.equals(PacketUtil.Packets.CLIENT_ENTITY_ACTION)){
            user.getChecks().forEach(check -> {
                check.exemptionTicks = 4; // 1000 until client accepts idk
            });
        }
        if(p.equals(PacketUtil.Packets.TELEPORT_CLIENT_CONFIRM)){
            //user.toBukkit().sendMessage("Client confirmed my teleport! Resetting all check's exemption to 5");
            user.getChecks().forEach(check -> {
                check.exemptionTicks = 5; // 5 cuz client accepted
            });
        }
        user.getChecks().forEach(check -> {
           check.onPacket(event);
        });
        // run client processors
        user.getProcessorManager().getProcessors().forEach(processor -> processor.onPacket(event));
    }


    //Serverbound
    @Override
    public void onPacketSend(PacketSendEvent event) {
        PacketUtil.Packets p = PacketUtil.toPacketSend(event);

        Player player = (Player) event.getPlayer();

        if (player == null) {
            return;
        }

        CookiePlayer user = CookieAC.getPlayerManager().getUser(player);

        if (user == null) {
            return;
        }

        if(p.equals(PacketUtil.Packets.SERVER_POSITION)){
            //user.toBukkit().sendMessage("Server sent a teleport request! Setting all check's exemption to 1000");
            user.getChecks().forEach(check -> {
                check.exemptionTicks = 1000; // 1000 until client accepts idk
            });
        }
        //Register serverbound packets for checks
        user.getChecks().forEach(check -> {
            check.onPacket(event);
        });


        //run server processors
        user.getProcessorManager().getProcessors().forEach(processor -> processor.onPacket(event));
    }

}
