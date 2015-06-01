package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.cmd.Command;

public class Teleport extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        s.send(new ClientChatPacket("/tp " + args[1]));
    }

    @Override public String getCommand() {
        return "tp";
    }

    @Override public String getDescription() {
        return "Teleport";
    }
}
