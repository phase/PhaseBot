package xyz.jadonfowler.phasebot.cmd.position;

import java.util.Arrays;
import java.util.UUID;
import org.spacehq.packetlib.Session;
import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;
import xyz.jadonfowler.phasebot.entity.Entity;
import xyz.jadonfowler.phasebot.entity.Player;
import xyz.jadonfowler.phasebot.util.UUIDFetcher;

public class Move extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        double rx, ry, rz;
        if (args.length == 2) {
            if (args[1].matches("[-+]?\\d*\\.?\\d+")) {
                int id = Integer.parseInt(args[1]);
                Entity e = Entity.byId(id);
                PhaseBot.getBot().moveTo(e);
            }
            else {
                try {
                    UUIDFetcher uf = new UUIDFetcher(Arrays.asList(args[1]));
                    UUID u = uf.call().get(args[1]);
                    Player p = Player.byUUID(u);
                    PhaseBot.getBot().moveTo(p);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return;
        }
        if (args[1].contains("*")) rx = Double.parseDouble(args[1].replace("*", "")) - PhaseBot.getBot().pos.x;
        else rx = Double.parseDouble(args[1]);
        if (args[2].contains("*")) ry = Double.parseDouble(args[2].replace("*", "")) - PhaseBot.getBot().pos.y;
        else ry = Double.parseDouble(args[2]);
        if (args[3].contains("*")) rz = Double.parseDouble(args[3].replace("*", "")) - PhaseBot.getBot().pos.z;
        else rz = Double.parseDouble(args[3]);
        // PhaseBot.getConsole().println("Move: " + (PhaseBot.getBot().pos.x +
        // rx) + " " + (PhaseBot.getBot().pos.y + ry) + " "
        // + (PhaseBot.getBot().pos.z + rz));
        PhaseBot.getBot().move(rx, ry, rz);
    }

    @Override public String getCommand() {
        return "move";
    }

    @Override public String getDescription() {
        return "Move the bot to a position.";
    }
}
