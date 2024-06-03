package fr.clue.cookieac.check;

import fr.clue.cookieac.CookieAC;
import fr.clue.cookieac.event.Event;
import fr.clue.cookieac.player.CookiePlayer;
import lombok.Getter;
import lombok.Setter;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;

public class Check extends Event {
    @Setter @Getter
    private CookiePlayer user;
    private CheckData data;
    private double violations;
    private double punishmentVL;
    private String checkName, checkType;
    private boolean enabled;
    private boolean experimental;

    public Check() {
        if (getClass().isAnnotationPresent(CheckData.class)) {
            this.data = getClass().getAnnotation(CheckData.class);
            this.punishmentVL = this.data.punishmentVL();
            this.checkName = this.data.name();
            this.checkType = this.data.type();
            this.enabled = this.data.enabled();
            this.experimental = this.data.experimental();
        }
    }

    public void fail(String... data) {
        this.violations += 1.0;
        String checkType = this.checkType;

        if (!this.experimental) {
            checkType += "*";
        }

        String alert = NamedTextColor.GOLD + "Cookie > "
                + NamedTextColor.WHITE + this.user.toBukkit().getName()
                + NamedTextColor.GRAY + " failed "
                + NamedTextColor.WHITE + this.checkName
                + NamedTextColor.DARK_GRAY + " (" + NamedTextColor.WHITE + checkType + NamedTextColor.DARK_GRAY + ")"

                + NamedTextColor.DARK_GRAY + " (" + NamedTextColor.RED + this.violations + NamedTextColor.DARK_GRAY
                + "/" + NamedTextColor.RED + this.punishmentVL + NamedTextColor.DARK_GRAY + ")";

        TextComponent textComponent = Component.text(alert);
        CookieAC.getPlayerManager().getUsers().stream().filter(
                user -> user.toBukkit().isOp()
        ).forEach(u -> u.toBukkit().sendMessage(textComponent));
    }

}
