package xyz.jadonfowler.phasebot.cmd.fun;

import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;

public class DerpCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        if (PhaseBot.getBot().isDerp) {
            PhaseBot.getBot().isDerp = false;
        }
        else {
            PhaseBot.getBot().isDerp = true;
            PhaseBot.getBot().derp(s);
        }
    }

    @Override public String getCommand() {
        return "derp";
    }

    @Override public String getDescription() {
        return "derp";
    }
}
