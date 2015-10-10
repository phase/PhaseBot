package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.util.*;

public class GetCloseToCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        double rx, ry, rz;
        if (args[1].contains("*")) rx = Double.parseDouble(args[1].replace("*", "")) - PhaseBot.getBot().pos.x;
        else rx = Double.parseDouble(args[1]);
        if (args[2].contains("*")) ry = Double.parseDouble(args[2].replace("*", "")) - PhaseBot.getBot().pos.y;
        else ry = Double.parseDouble(args[2]);
        if (args[3].contains("*")) rz = Double.parseDouble(args[3].replace("*", "")) - PhaseBot.getBot().pos.z;
        else rz = Double.parseDouble(args[3]);
        PhaseBot.getBot().getCloseTo(new Vector3d(rx, ry, rz));
    }

    @Override public String getCommand() {
        return "getcloseto";
    }

    @Override public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
}
