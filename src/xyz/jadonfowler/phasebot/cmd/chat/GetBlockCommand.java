package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;
import xyz.jadonfowler.phasebot.world.*;

public class GetBlockCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        int rx = Integer.parseInt(args[1]);
        int ry = Integer.parseInt(args[2]);
        int rz = Integer.parseInt(args[3]);
        Material m = Block
                .getBlock(PhaseBot.getBot().pos.x + rx, PhaseBot.getBot().pos.y + ry, PhaseBot.getBot().pos.z + rz)
                .getMaterial();
        // PhaseBot.getBot().say("I am standing on: " + m.toString());
        PhaseBot.getBot().getVariables().put("block", m.getId() + "");
    }

    @Override public String getCommand() {
        return "getblock";
    }

    @Override public String getDescription() {
        return null;
    }
}
