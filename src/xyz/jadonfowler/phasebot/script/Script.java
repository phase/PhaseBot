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
                try {
                    String[] args = s.split(" ");
                    if (args[0].equalsIgnoreCase(".for")) {
                        Integer lines = Integer.parseInt(args[1]);
                        Integer amount = Integer.parseInt(args[2]);
                        System.out.println("FOR LOOP: " + lines + " " + amount);
                        for (int a = 0; a < amount; a++) {
                            for (int j = 0; j < lines; j++) {
                                String command = this.lines[j + i + 1];
                                PhaseBot.getBot().runCommand(command.replace(".i", a + ""));
                                Thread.sleep(200);
                            }
                        }
                        i += lines;
                    }
                }
                catch (Exception e) {}
            }
            else {
                PhaseBot.getBot().runCommand(s);
                try {
                    Thread.sleep(200);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
