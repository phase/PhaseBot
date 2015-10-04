package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;

public class ReloadCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        PhaseBot.getBot().say("Reloading...");
        PhaseBot.getCommandManager().clearCommands();
        PhaseBot.registerCommands();
        PhaseBot.reloadScripts();
    }

    @Override public String getCommand() {
        return "reload";
    }

    @Override public String getDescription() {
        return "Reload PhaseBot";
    }
}
