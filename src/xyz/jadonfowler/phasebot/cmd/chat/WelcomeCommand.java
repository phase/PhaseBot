package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.cmd.Command;

public class WelcomeCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        try {
            String to = args[1];
            String[] messages = { "&l Welcome to the ORE!", "To get started, make a thread", "on the forums to apply",
                    "so you can become a builder!" };
            for (String m : messages)
                s.send(new ClientChatPacket("/msg " + to + " " + m));
        }
        catch (Exception e) {}
    }

    @Override public String getCommand() {
        return "welcome";
    }

    @Override public String getDescription() {
        return "Welcome someone to the server!";
    }
}
