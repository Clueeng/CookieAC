package fr.clue.cookieac.check;

import com.github.retrooper.packetevents.event.PacketReceiveEvent;
import fr.clue.cookieac.CookieAC;
import fr.clue.cookieac.command.EnableAlerts;
import fr.clue.cookieac.event.Event;
import fr.clue.cookieac.player.CookiePlayer;
import fr.clue.cookieac.utils.CollisionUtils;
import fr.clue.cookieac.utils.PacketUtil;
import fr.clue.cookieac.utils.PunishmentUtils;
import fr.clue.cookieac.utils.VersionUtil;
import fr.clue.cookieac.utils.location.FlyingLocation;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

import java.util.Arrays;

public class Check extends Event {
    @Setter @Getter
    private CookiePlayer user;
    private CheckData data;
    public double violations;
    private double punishmentVL;
    public String checkName, checkType, description;
    private boolean enabled;
    private boolean experimental, setback;
    public int exemptionTicks;

    public Check() {
        if (getClass().isAnnotationPresent(CheckData.class)) {
            this.data = getClass().getAnnotation(CheckData.class);
            this.punishmentVL = this.data.punishmentVL();
            this.checkName = this.data.name();
            this.checkType = this.data.type();
            this.enabled = this.data.enabled();
            this.experimental = this.data.experimental();
            this.setback = this.data.setback();
            this.description = this.data.description();
        }
    }
    public void setback(){
        if(this.setback){
            Location l = getUser().getProcessorManager().getMovementProcessor().getLastGroundPosition().toLocation(getUser().toBukkit().getWorld());
            if(l.getX() == 0 && l.getY() == 0 && l.getZ() == 0){
                return;
            }
            Bukkit.getScheduler().runTask(CookieAC.getInstance(),r -> {
                getUser().toBukkit().teleport(l);
                l.add(0, CollisionUtils.calculateDistanceToGround(getUser().toBukkit()),0);
                getUser().toBukkit().setSprinting(false);
            });
        }
    }

    public void exemptOnJoin(){
        long whenJoined = System.currentTimeMillis() - getUser().getTimeJoin();
        boolean justJoined = whenJoined <= 1000;
        if(justJoined){
            this.exemptionTicks = (int) (whenJoined / 20) + 1;
        }
    }

    public void fail(PacketReceiveEvent event, String... data) {
        if(isCheckExempted()) return;
        this.violations += 1.0;
        String checkType = this.checkType;
        if (this.experimental) {
            checkType += "*";
        }
        if(this.setback){
            setback();
            switch (PacketUtil.toPacketReceive(event)) {
                case CLIENT_LOOK:
                case CLIENT_BLOCK_PLACE:
                case CLIENT_ENTITY_ACTION:
                case CLIENT_POSITION:
                case CLIENT_POSITION_LOOK:
                case CLIENT_FLYING: {
                    event.setCancelled(true);
                    break;
                }
            }
        }
        Component textComponent = Component.text(CookieAC.cookieName, NamedTextColor.GOLD).append(
                Component.text(" > ",NamedTextColor.YELLOW).append(
                        Component.text("[" + VersionUtil.formattedVersion(VersionUtil.getViaVer(getUser().toBukkit())) + "] ")
                ).append(
                Component.text(user.toBukkit().getName(), NamedTextColor.WHITE)).append(
                Component.text(" failed ", NamedTextColor.GRAY).append(
                Component.text(this.checkName + " " + checkType, NamedTextColor.WHITE).append(
                Component.text(" (VL: " + ((double)(Math.round(this.violations * 100f))/100f) + "/" + this.punishmentVL + ")", NamedTextColor.RED)
        ))));
        Component hover = Component.text(Arrays.toString(data));
        textComponent = textComponent.hoverEvent(HoverEvent.showText(hover));
        Component finalTextComponent = textComponent; // cuz lambda shit
        if(EnableAlerts.MODE == 1){
            CookieAC.getPlayerManager().getUsers().stream().filter(
                    user -> user.toBukkit().isOp()
            ).forEach(u -> u.toBukkit().sendMessage(finalTextComponent));
        }
        if(EnableAlerts.MODE == 0){
            CookieAC.getPlayerManager().getUsers()
                    .forEach(u -> u.toBukkit().sendMessage(finalTextComponent));
        }

        if(this.violations >= this.punishmentVL){
            Component banComponent = Component.text(CookieAC.cookieName, NamedTextColor.GOLD).append(
                    Component.text(" > ",NamedTextColor.YELLOW).append(
                            Component.text(user.toBukkit().getName(), NamedTextColor.WHITE)).append(
                            Component.text(" was punished for failing ", NamedTextColor.GRAY).append(
                                    Component.text(this.checkName + " " + checkType, NamedTextColor.WHITE).append(
                                            Component.text(" too many times", NamedTextColor.GRAY)
                                    ))));

            CookieAC.getPlayerManager().getUsers().stream().filter(
                    user -> user.toBukkit().isOp()
            ).forEach(u -> u.toBukkit().sendMessage(banComponent));
            PunishmentUtils.punish(user, PunishmentUtils.PunishType.KICK, "You were caught cheating");
            violations = 0.0;
        }
    }

    public void updateExemptionTicks(){
        this.exemptionTicks = Math.max(0, this.exemptionTicks - 1);
    }
    public boolean isCheckExempted(){
        return exemptionTicks > 0;
    }

}
