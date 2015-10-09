package xyz.jadonfowler.phasebot.cmd;

import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.PhaseBot;

public abstract class Command {

    public Command() {
        PhaseBot.getCommandManager().addCommand(this);
        //PhaseBot.getConsole().println("  Command " + getCommand() + " initialized!");
    }

    public abstract void exec(String in, String[] args, Session s);

    public abstract String getCommand();

    public abstract String getDescription();

    public String toString() {
        return getCommand();
    }
}
