package fr.clue.cookieac.process;

import fr.clue.cookieac.event.Event;
import fr.clue.cookieac.player.CookiePlayer;
import lombok.Getter;
import org.bukkit.Material;

@Getter
public class Processor extends Event {

    private final String name;


    private final CookiePlayer data;

    public Processor(final CookiePlayer data) {
        this.name = getClass().getAnnotation(ProcessorInfo.class).name();
        this.data = data;
    }
}