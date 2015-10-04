package xyz.jadonfowler.phasebot;

import lombok.*;

public class ChatMessage {

    @Getter @Setter private String message = null;
    @Getter @Setter private String sender = null;
    @Getter private String command = null;

    public ChatMessage(String full) {
        try {
            if (full.matches("\\[(.*)\\](.*): (.*)")) {
                sender = full.split("]")[1].split(": ")[0];
                message = full.split(": ")[1];
            }
            if (full.matches("\\[\\[(.*)\\](.*) -> me\\] (.*)")) {
                sender = full.split("]")[1].split(" ")[0];
                // private messages don't need to put prefix
                message = PhaseBot.getPrefix() + full.split("] ")[1];
            }
            if (PhaseBot.getPrefix().equals(".")) message = "." + message;
            if (message != null && message.startsWith(PhaseBot.getPrefix()))
                command = message.split(PhaseBot.getPrefix())[1].split(" ")[0];
        }
        catch (Exception e) {}
    }

    public boolean isCommand() {
        return command != null;
    }
}
