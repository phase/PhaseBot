package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;

public class MessageCommand extends Command{

    @Override public void exec(String in, String[] args, Session s) {
        String to = args[1];
        String m = "";
        for(int i = 2; i < args.length; i++){
            m += args[i] + " ";
        }
        PhaseBot.getBot().say("/msg " + to + " " + m);
    }

    @Override public String getCommand() {
        return "msg";
    }

    @Override public String getDescription() {
        return "Message a player";
    }}
