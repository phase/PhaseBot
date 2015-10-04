package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;


public class SudoCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        String m = "";
        for(int i = 1; i < args.length; i++){
            m += args[i] + " ";
        }
        PhaseBot.getBot().say(m);
    }

    @Override public String getCommand() {
        return "sudo";
    }

    @Override public String getDescription() {
        return "sudo a command";
    }
}
