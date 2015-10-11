package xyz.jadonfowler.phasebot.script;

import lombok.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import org.spacehq.packetlib.*;

public class Script {

    @Getter private final String[] lines;
    @Getter private String name;
    public int PC = 0;
    @Getter @Setter private static boolean stop = false;

    public Script(String name, String... lines) {
        this.lines = lines;
        this.name = name;
    }

    public void run(String[] inputArguments) {
        PC = 0;
        while (PC != lines.length) {
            if (stop) { return; }
            // PhaseBot.getConsole().println(PC + "/" + lines.length);
            parseLine(lines[PC], inputArguments);
        }
    }

    public void parseLine(String s, String[] inputArguments) {
        if (stop) { return; }
        try {
            String[] args = s.split(" ");
            if (s.trim().startsWith(";")) {/* Comment */}
            else if (args[0].equalsIgnoreCase(".for")) {
                int lines = Integer.parseInt(args[1]);
                int amount = Integer.parseInt(args[2]);
                // PhaseBot.getConsole().println("FOR LOOP: " + lines + " "
                // + amount);
                loop:
                for (int a = 0; a < amount; a++) {
                    for (int j = 0; j < lines; j++) {
                        String command = this.lines[j + PC + 1];
                        if (command.equalsIgnoreCase(".break")) break loop;
                        command = replaceArguments(command, inputArguments).replace("@i", a + "").trim();
                        // PhaseBot.getConsole().println(command + " : " +
                        // PC);
                        command = replaceVariables(command);
                        PhaseBot.getBot().runCommand(command);
                        Thread.sleep(5);
                    }
                }
                PC += lines;
            }
            else if (args[0].equalsIgnoreCase(".ifeq")) {
                int lines = Integer.parseInt(args[1]);
                String a = args[2];
                a = replaceArguments(a, inputArguments);
                if (a.contains("@")) {
                    a = a.replace("@", "");
                    a = PhaseBot.getBot().getVariables().get(a);
                }
                String b = args[3];
                b = replaceArguments(b, inputArguments);
                if (b.contains("@")) {
                    b = b.replace("@", "");
                    b = PhaseBot.getBot().getVariables().get(b);
                }
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
            else if (args[0].equalsIgnoreCase(".define")) {
                int lines = Integer.parseInt(args[1]);
                final String name = args[2];
                String[] script = new String[lines];
                for (int j = 0; j < lines; j++) {
                    String command = this.lines[j + PC + 1];
                    script[j] = command;
                }
                Script macro = new Script(name, script);
                PhaseBot.scripts.add(macro);
                new Command() {

                    public void exec(String in, String[] args, Session s) {
                        for (Script t : PhaseBot.scripts) {
                            if (t.getName().equalsIgnoreCase(args[0])) {
                                t.run(args);
                                return;
                            }
                        }
                    }

                    public String getCommand() {
                        return name;
                    }

                    public String getDescription() {
                        return null;
                    }
                };
                PC += lines;
            }
            // .val derp = I like @type
            else if (args[0].equalsIgnoreCase(".val")) {
                String id = args[1];
                String value = "";
                for (int j = 3; j < args.length; j++) {
                    String temp = args[j];
                    if (temp.startsWith("@a")) temp = replaceArguments(temp, inputArguments);
                    if (temp.startsWith("@")) temp = replaceVariables(temp);
                    value += temp + " ";
                }
                // System.out.println(id + ": " + value);
                PhaseBot.getBot().getVariables().put(id.replace("@", ""), value.trim());
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
        catch (Exception e) {
            e.printStackTrace();
        }
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

    public static String replaceVariables(String command) {
        for (String h : command.split(" ")) {
            if (h.startsWith("@")) {
                // PhaseBot.getConsole().println(h + " : " + command + " : " +
                // PC);
                h = h.replace("@", "");
                // PhaseBot.getConsole().println(command);
                try {
                    command = command.replace("@" + h, PhaseBot.getBot().getVariables().get(h));
                }
                catch (Exception e) {}
            }
        }
        return command;
    }
}
