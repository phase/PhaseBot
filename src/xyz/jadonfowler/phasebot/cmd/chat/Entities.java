package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.entity.*;

public class Entities extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        for (Entity e : Entity.getEntities().values())
            PhaseBot.getConsole().println(e);
    }

    @Override public String getCommand() {
        return "entities";
    }

    @Override public String getDescription() {
        return "Print the entities";
    }
}
