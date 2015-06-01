package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.mc.protocol.packet.ingame.client.ClientChatPacket;
import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;

public class Patrol extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        double times = Double.parseDouble(args[1]);
        for (int i = 0; i < times; i++) {
            PhaseBot.getBot().move(Math.abs(PhaseBot.getBot().positions[0].x - PhaseBot.getBot().pos.x),
                    Math.abs(PhaseBot.getBot().positions[0].y - PhaseBot.getBot().pos.y),
                    Math.abs(PhaseBot.getBot().positions[0].z - PhaseBot.getBot().pos.z));
            PhaseBot.getBot().move(Math.abs(PhaseBot.getBot().positions[1].x - PhaseBot.getBot().pos.x),
                    Math.abs(PhaseBot.getBot().positions[1].y - PhaseBot.getBot().pos.y),
                    Math.abs(PhaseBot.getBot().positions[1].z - PhaseBot.getBot().pos.z));
            PhaseBot.getBot().move(Math.abs(PhaseBot.getBot().positions[2].x - PhaseBot.getBot().pos.x),
                    Math.abs(PhaseBot.getBot().positions[2].y - PhaseBot.getBot().pos.y),
                    Math.abs(PhaseBot.getBot().positions[2].z - PhaseBot.getBot().pos.z));
            PhaseBot.getBot().move(Math.abs(PhaseBot.getBot().positions[3].x - PhaseBot.getBot().pos.x),
                    Math.abs(PhaseBot.getBot().positions[3].y - PhaseBot.getBot().pos.y),
                    Math.abs(PhaseBot.getBot().positions[3].z - PhaseBot.getBot().pos.z));
        }
        PhaseBot.getBot().move(Math.abs(PhaseBot.getBot().positions[0].x - PhaseBot.getBot().pos.x),
                Math.abs(PhaseBot.getBot().positions[0].y - PhaseBot.getBot().pos.y),
                Math.abs(PhaseBot.getBot().positions[0].z - PhaseBot.getBot().pos.z));
        s.send(new ClientChatPacket("I have finished patrolling!"));
    }

    @Override public String getCommand() {
        return "patrol";
    }

    @Override public String getDescription() {
        return "Patrols an area.";
    }
}
