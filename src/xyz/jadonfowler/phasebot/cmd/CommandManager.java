package xyz.jadonfowler.phasebot.cmd;

import java.util.ArrayList;
import org.spacehq.packetlib.Session;

public class CommandManager {

    private ArrayList<Command> commands;

    public CommandManager() {
        commands = new ArrayList<Command>();
    }

    public Command addCommand(Command c) {
        commands.add(c);
        return c;
    }

    private String latestIn;
    private String[] latestArgs;
    private Session latestSession;

    public void performCommand(String in, String[] args, Session s) {
        for (Command c : commands) {
            if (c.getCommand().equalsIgnoreCase(args[0].replace(".", ""))) {
                latestIn = in;
                latestArgs = args;
                latestSession = s;
                c.exec(in, args, s);
            }
        }
    }

    public void performLastCommand() {
        performCommand(latestIn, latestArgs, latestSession);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
}
