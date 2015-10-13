package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.world.material.*;

public class GetTypeCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        PhaseBot.getBot().getVariables().put("type",
                Material.getMaterial(Integer.parseInt(args[1])).getDisplayName().toLowerCase());
    }

    @Override public String getCommand() {
        return "gettype";
    }

    @Override public String getDescription() {
        return "Get the type of an id";
    }
}
