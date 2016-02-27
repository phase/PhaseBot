package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;

public class LookCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        float p = Float.parseFloat(args[1]);
        float y = Float.parseFloat(args[2]);
        PhaseBot.getBot().look(y, p);
    }

    @Override public String getCommand() {
        return "look";
    }

    @Override public String getDescription() {
        return "Set the bot's pitch and yaw.";
    }
}
