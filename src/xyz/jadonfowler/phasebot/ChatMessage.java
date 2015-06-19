package xyz.jadonfowler.phasebot;

import lombok.Getter;

public class ChatMessage {
    @Getter private String message = null;
    @Getter private String sender = null;

    public ChatMessage(String full) {
        if (full.matches("\\[(.*)\\](.*):(.*)")) {
            sender = full.split("]")[1].split(": ")[0];
            message = full.split(": ")[1];
            System.out.println("Got message: " + sender + "::" + message);
        }
    }
}
