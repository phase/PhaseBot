package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.mc.protocol.data.game.*;
import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.inventory.*;


public class InventoryCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        Inventory inv = PhaseBot.getBot().getInventory();
        String items = " ";
        for(ItemStack i : inv.getItems())
            if (i != null)
            items += i.getId() + ", ";
        PhaseBot.getBot().say("My inventory contains:" + items);
    }

    @Override public String getCommand() {
        return "inv";
    }

    @Override public String getDescription() {
        return "Check inventory";
    }
}
