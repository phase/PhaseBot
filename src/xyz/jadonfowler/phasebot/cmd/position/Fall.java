package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;
import xyz.jadonfowler.phasebot.world.*;

public class Fall extends Command {

    @Override public void exec(String in, String[] args, Session ses) {
        while (Block.getBlock(PhaseBot.getBot().pos.x, PhaseBot.getBot().pos.y - 1, PhaseBot.getBot().pos.z).getMaterial() == Materials.AIR) {
            PhaseBot.getBot().move(0, -1, 0);
        }
    }

    @Override public String getCommand() {
        return "fall";
    }

    @Override public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
}
