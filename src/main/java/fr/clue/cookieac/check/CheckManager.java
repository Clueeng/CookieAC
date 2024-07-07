package fr.clue.cookieac.check;
import fr.clue.cookieac.check.impl.gravity.GravityA;
import fr.clue.cookieac.check.impl.gravity.GravityB;
import fr.clue.cookieac.check.impl.ground.GroundA;
import fr.clue.cookieac.check.impl.ground.GroundB;
import fr.clue.cookieac.player.CookiePlayer;

import java.util.ArrayList;
import java.util.List;

public class CheckManager {
    public final List<Check> checks = new ArrayList<>();

    public void loadChecks() {
        this.checks.add(new GroundA());
        this.checks.add(new GroundB());
        this.checks.add(new GravityA());
        this.checks.add(new GravityB());
    }

    public void loadToPlayer(CookiePlayer user) {
        wrapUser(user);
        user.getChecks().addAll(this.checks);
    }

    public void wrapUser(CookiePlayer user) {
        this.checks.forEach(check -> check.setUser(user));
    }
}
