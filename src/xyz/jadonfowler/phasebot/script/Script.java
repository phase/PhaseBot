package xyz.jadonfowler.phasebot.script;

import lombok.*;
import xyz.jadonfowler.phasebot.*;

public class Script {

    @Getter private final String[] lines;
    @Getter private String name;
    public int PC = 0;

    public Script(String name, String... lines) {
        this.lines = lines;
        this.name = name;
    }

    public void run(String[] inputArguments) {
        PC = 0;
        while(PC != lines.length) {
            //System.out.println(PC + "/" + lines.length);
            parseLine(lines[PC], inputArguments);
        }
    }

    public void parseLine(String s, String[] inputArguments) {
        if (s.startsWith(".")) {
            try {
                String[] args = s.split(" ");
                if (args[0].equalsIgnoreCase(".for")) {
                    Integer lines = Integer.parseInt(args[1]);
                    Integer amount = Integer.parseInt(args[2]);
                    System.out.println("FOR LOOP: " + lines + " " + amount);
                    loop:
                    for (int a = 0; a < amount; a++) {
                        for (int j = 0; j < lines; j++) {
                            String command = this.lines[j + PC + 1];
                            if (command.equalsIgnoreCase(".break")) break loop;
                            command = replaceArguments(command, inputArguments).replace("@i", a + "").trim();
                            //System.out.println(command + " : " + PC);
                            command = replaceVariables(command);
                            PhaseBot.getBot().runCommand(command);
                            Thread.sleep(5);
                        }
                    }
                    PC += lines;
                }
                else if (args[0].equalsIgnoreCase(".ifeq")) {
                    Integer lines = Integer.parseInt(args[1]);
                    String a = args[2];
                    a = replaceArguments(a, inputArguments);
                    if (a.contains("@")) {
                        a = a.replace("@", "");
                        a = PhaseBot.getBot().getVariables().get(a);
                    }
                    String b = args[3];
                    if (a.equals(b)) {
                        for (int j = 0; j < lines; j++) {
                            String command = this.lines[j + PC + 1];
                            command = replaceArguments(command, inputArguments).trim();
                            command = replaceVariables(command);
                            PhaseBot.getBot().runCommand(command);
                            Thread.sleep(20);
                        }
                    }
                    PC += lines;
                }
            }
            catch (Exception e) {}
        }
        else {
            String command = replaceArguments(s, inputArguments);
            command = replaceVariables(command);
            PhaseBot.getBot().runCommand(command);
            try {
                Thread.sleep(5);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        PC++;
    }

    private String replaceArguments(String s, String[] args) {
        for (int i = 0; i < args.length; i++) {
            try {
                s = s.replace("@a" + i, args[i + 1]);
            }
            catch (Exception e) {}
        }
        return s;
    }

    private String replaceVariables(String command) {
        for (String h : command.split(" ")) {
            if (h.startsWith("@")) {
               // System.out.println(h + " : " + command + " : " + PC);
                h = h.replace("@", "");
                // System.out.println(command);
                try {
                    command = command.replace("@" + h, PhaseBot.getBot().getVariables().get(h));
                }
                catch (Exception e) {}
            }
        }
        return command;
    }
}
