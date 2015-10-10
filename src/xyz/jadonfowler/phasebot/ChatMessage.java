package xyz.jadonfowler.phasebot;

import lombok.*;

public class ChatMessage {

    @Getter @Setter private String message = null;
    @Getter @Setter private String sender = null;
    @Getter private String command = null;

    public ChatMessage(String full) {
        try {
            //PhaseBot.getConsole().println(full);
            if (full.matches("\\[(.*)\\](.*): (.*)")) {
                sender = full.split("]")[1].split(": ")[0];
                message = full.split(": ")[1];
            }
            else if (full.matches("\\[\\[(.*)\\](.*) -> me\\] (.*)")) {
                sender = full.split("]")[1].split(" ")[0];
                // private messages don't need to put prefix
                message = PhaseBot.getPrefix() + full.split("] ")[1];
            }
            else if (full.matches("<(.+)> (.+)")) {
                sender = full.split("<")[1].split(">")[0];
                message = full.split("> ")[1];
                //PhaseBot.getConsole().println(sender + ": " + message);
            }
            
            if (message != null && message.startsWith(PhaseBot.getPrefix()))
                command = message.replaceFirst(PhaseBot.getPrefix(), "").split(" ")[0];
        }
        catch (Exception e) {}
    }

    public boolean isCommand() {
        return command != null;
    }
}
