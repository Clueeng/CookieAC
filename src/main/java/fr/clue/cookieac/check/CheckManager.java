package fr.clue.cookieac.check;
import fr.clue.cookieac.check.impl.TestCheck;
import fr.clue.cookieac.player.CookiePlayer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class CheckManager {
    public final List<Check> checks = new ArrayList<>();

    public void loadChecks() {
        this.checks.add(new TestCheck());
    }

    public void loadToPlayer(CookiePlayer user) {
        wrapUser(user);
        user.getChecks().addAll(this.checks);
    }

    public void wrapUser(CookiePlayer user) {
        this.checks.forEach(check -> check.setUser(user));
    }
}
