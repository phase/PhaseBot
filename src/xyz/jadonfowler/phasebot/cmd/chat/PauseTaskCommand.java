package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.script.*;


public class PauseTaskCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        Script.setStop(!Script.isStop());
    }

    @Override public String getCommand() {
        return "pause";
    }

    @Override public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
}
