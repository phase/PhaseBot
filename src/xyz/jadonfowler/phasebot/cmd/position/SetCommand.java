package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;

public class SetCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        switch (args[1]) {
        case "p0":
            PhaseBot.getBot().positions[0] = PhaseBot.getBot().pos;
            s.send(new ClientChatPacket("p0 is set to " + (int) PhaseBot.getBot().pos.x + " "
                    + (int) PhaseBot.getBot().pos.y + " " + (int) PhaseBot.getBot().pos.z));
            break;
        case "p1":
            PhaseBot.getBot().positions[1] = PhaseBot.getBot().pos;
            s.send(new ClientChatPacket("p1 is set to " + (int) PhaseBot.getBot().pos.x + " "
                    + (int) PhaseBot.getBot().pos.y + " " + (int) PhaseBot.getBot().pos.z));
            break;
        case "p2":
            PhaseBot.getBot().positions[2] = PhaseBot.getBot().pos;
            s.send(new ClientChatPacket("p2 is set to " + (int) PhaseBot.getBot().pos.x + " "
                    + (int) PhaseBot.getBot().pos.y + " " + (int) PhaseBot.getBot().pos.z));
            break;
        case "p3":
            PhaseBot.getBot().positions[3] = PhaseBot.getBot().pos;
            s.send(new ClientChatPacket("p3 is set to " + (int) PhaseBot.getBot().pos.x + " "
                    + (int) PhaseBot.getBot().pos.y + " " + (int) PhaseBot.getBot().pos.z));
            break;
        }
    }

    @Override public String getCommand() {
        return "set";
    }

    @Override public String getDescription() {
        return "Sets positions";
    }
}
