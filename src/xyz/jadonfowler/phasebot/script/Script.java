package xyz.jadonfowler.phasebot.script;

import lombok.*;
import xyz.jadonfowler.phasebot.*;

public class Script implements Runnable {

    @Getter private final String[] lines;
    @Getter private String name;

    public Script(String name, String... lines) {
        this.lines = lines;
        this.name = name;
    }

    @Override public void run() {
        for (int i = 0; i < lines.length; i++) {
            String s = lines[i];
            if (s.startsWith(".")) {
                String[] args = s.split(" ");
                
            }
            else PhaseBot.getBot().runCommand(s);
        }
    }
}
