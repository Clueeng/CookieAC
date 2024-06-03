package fr.clue.cookieac.process;

import fr.clue.cookieac.player.CookiePlayer;
import fr.clue.cookieac.process.impl.MovementProcessor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ProcessorManager {
    private final List<Processor> processors = new ArrayList<>();

    private final MovementProcessor movementProcessor;

    public ProcessorManager(CookiePlayer user) {
        this.processors.add(this.movementProcessor = new MovementProcessor(user));
    }
}