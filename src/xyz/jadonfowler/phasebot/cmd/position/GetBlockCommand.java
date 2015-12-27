package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.world.*;
import xyz.jadonfowler.phasebot.world.material.*;

public class GetBlockCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        int rx = Integer.parseInt(args[1]);
        int ry = Integer.parseInt(args[2]);
        int rz = Integer.parseInt(args[3]);
        BlockType m = Block
                .getBlock(PhaseBot.getBot().pos.x + rx, PhaseBot.getBot().pos.y + ry, PhaseBot.getBot().pos.z + rz)
                .getMaterial();
        // PhaseBot.getBot().say("I am standing on: " + m.getDisplayName());
        PhaseBot.getConsole().log(" getblock: " + m.getDisplayName() + " - " + m.getId());
        PhaseBot.getBot().getVariables().put("block", m.getId() + "");
    }

    @Override public String getCommand() {
        return "getblock";
    }

    @Override public String getDescription() {
        return null;
    }
}