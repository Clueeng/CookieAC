package fr.clue.cookieac.check;

import fr.clue.cookieac.CookieAC;
import fr.clue.cookieac.event.Event;
import fr.clue.cookieac.player.CookiePlayer;
import fr.clue.cookieac.utils.CollisionUtils;
import fr.clue.cookieac.utils.PunishmentUtils;
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
    public String checkName, checkType;
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
        }
    }
    public void setback(){
        if(this.setback){
            Location l = getUser().getProcessorManager().getMovementProcessor().getLastGroundPosition().toLocation(getUser().toBukkit().getWorld());
            Bukkit.getScheduler().runTask(CookieAC.getInstance(),r -> {
                getUser().toBukkit().teleport(l);
                l.add(0, CollisionUtils.calculateDistanceToGround(getUser().toBukkit())-0.05,0);
                getUser().toBukkit().setSprinting(false);

            });
        }
    }
    public void fail(String... data) {
        if(isCheckExempted()) return;
        this.violations += 1.0;
        String checkType = this.checkType;

        if (this.experimental) {
            checkType += "*";
        }
        if(this.setback){
            setback();
        }
        Component textComponent = Component.text(CookieAC.cookieName, NamedTextColor.GOLD).append(
                Component.text(" > ",NamedTextColor.YELLOW).append(
                Component.text(user.toBukkit().getName(), NamedTextColor.WHITE)).append(
                Component.text(" failed ", NamedTextColor.GRAY).append(
                Component.text(this.checkName + " " + checkType, NamedTextColor.WHITE).append(
                Component.text(" (VL: " + this.violations + "/" + this.punishmentVL + ")", NamedTextColor.RED)
        ))));
        Component hover = Component.text(Arrays.toString(data));
        textComponent = textComponent.hoverEvent(HoverEvent.showText(hover));
        Component finalTextComponent = textComponent; // cuz lambda shit
        CookieAC.getPlayerManager().getUsers().stream().filter(
                user -> user.toBukkit().isOp()
        ).forEach(u -> u.toBukkit().sendMessage(finalTextComponent));

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
