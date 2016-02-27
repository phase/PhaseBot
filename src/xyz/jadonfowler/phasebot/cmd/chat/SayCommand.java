package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.cmd.Command;

public class SayCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        StringBuilder text = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            text.append(args[i] + " ");
        }
        s.send(new ClientChatPacket(text.toString()));
    }

    @Override public String getCommand() {
        return "say";
    }

    @Override public String getDescription() {
        return "Say what the user inputs.";
    }
}
