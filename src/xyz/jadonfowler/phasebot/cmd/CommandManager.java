package xyz.jadonfowler.phasebot.cmd;

import java.util.*;
import org.spacehq.packetlib.*;

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
                //PhaseBot.getConsole().println(in + " : " + Arrays.toString(args));
                latestIn = in;
                latestArgs = args;
                latestSession = s;
                c.exec(in, args, s);
                return;
            }
        }
    }

    public void performLastCommand() {
        performCommand(latestIn, latestArgs, latestSession);
    }

    public ArrayList<Command> getCommands() {
        return commands;
    }
    
    public void clearCommands(){
        commands = new ArrayList<Command>();
    }
}
