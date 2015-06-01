package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.cmd.Command;

public class Slap extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        s.send(new ClientChatPacket("/me slaps " + args[1]));
    }

    @Override public String getCommand() {
        return "slap";
    }

    @Override public String getDescription() {
        return "Slap a person";
    }
}
