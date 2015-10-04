package xyz.jadonfowler.phasebot.cmd.inv;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;

public class SlotCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        try {
            Integer slot = Integer.parseInt(args[1]);
            if (slot < 0 || slot > 8) {
                throw new Exception(args[1] + " is not a slot!");
            }
            else {
                PhaseBot.getBot().setSlot(slot);
            }
        }
        catch (Exception e) {
            PhaseBot.getBot().say(e.getMessage());
        }
    }

    @Override public String getCommand() {
        return "slot";
    }

    @Override public String getDescription() {
        return "Set the slot of the player";
    }
}
