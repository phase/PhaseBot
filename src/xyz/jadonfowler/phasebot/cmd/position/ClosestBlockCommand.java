package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.world.material.*;


public class ClosestBlockCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        try{
            Material m = Material.getMaterial(args[1]);
            PhaseBot.getBot().getClosestBlock(m, 20);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override public String getCommand() {
        return "closest";
    }

    @Override public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
}
