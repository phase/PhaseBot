package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.world.*;
import xyz.jadonfowler.phasebot.world.material.*;

public class FallCommand extends Command {

    @Override public void exec(String in, String[] args, Session ses) {
        while (Block.getBlock(PhaseBot.getBot().pos.x, PhaseBot.getBot().pos.y - 1, PhaseBot.getBot().pos.z).getMaterial() == Material.getMaterial(0)) {
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
