package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;

public class JumpCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        if (args.length == 4) {
            double x = Double.parseDouble(args[1]);
            double y = Double.parseDouble(args[2]);
            double z = Double.parseDouble(args[3]);
            PhaseBot.getBot().jump(x, y, z);
        }
        else if (args.length == 2) {
            double y = Double.parseDouble(args[1]);
            PhaseBot.getBot().jump(0, y, 0);
        }
    }

    @Override public String getCommand() {
        return "jump";
    }

    @Override public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
}
