package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.util.*;

public class PlaceBlockCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        try {
            int x = Integer.parseInt(args[1]);
            int y = Integer.parseInt(args[2]);
            int z = Integer.parseInt(args[3]);
            Vector3d v = PhaseBot.getBot().relativeToAbsolute(new Vector3d(x, y, z));
            //PhaseBot.getBot().say("I will place a block at " + v);
            PhaseBot.getBot().placeBlock(v);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public String getCommand() {
        return "place";
    }

    @Override public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
}
