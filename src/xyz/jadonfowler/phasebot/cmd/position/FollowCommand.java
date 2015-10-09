package xyz.jadonfowler.phasebot.cmd.position;

import java.util.*;
import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.entity.*;
import xyz.jadonfowler.phasebot.pathfind.*;
import xyz.jadonfowler.phasebot.pathfind.AStar.InvalidPathException;
import xyz.jadonfowler.phasebot.util.*;

public class FollowCommand extends Command {

    Player followed = null;

    @Override public void exec(String in, String[] args, Session s) {
        if (followed == null) {
            try {
                UUIDFetcher uf = new UUIDFetcher(Arrays.asList(args[1]));
                UUID u = uf.call().get(args[1]);
                followed = Player.byUUID(u);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            new Thread(new Runnable() {

                public void run() {
                    while (true) {
                        if (!PhaseBot.getBot().pathing) {
                            try {
                                Vector3d start = PhaseBot.getBot().getPos().clone().addY(-1);
                                Vector3d end = followed.getPos().clone().addY(-1);
                                PhaseBot.getConsole().println("start= " + start + " \nend= " + end);
                                // create our pathfinder
                                AStar path = null;
                                path = new AStar(start, end, 100);
                                // get the list of nodes to walk to as a Tile
                                // object
                                ArrayList<Tile> route = path.iterate();
                                // get the result of the path trace
                                PathingResult result = path.getPathingResult();
                                switch (result) {
                                case SUCCESS:
                                    // Path was successful. Do something here.
                                    PhaseBot.getBot().moveAlong(route);
                                    PhaseBot.getConsole().println("Path was found! :D");
                                    break;
                                case NO_PATH:
                                    // No path found, throw error.
                                    PhaseBot.getConsole().println("No path found!");
                                    break;
                                default:
                                    break;
                                }
                            }
                            catch (InvalidPathException e) {
                                // InvalidPathException will be thrown if start
                                // or
                                // end block is
                                // air
                                if (e.isEndNotSolid()) {
                                    PhaseBot.getConsole().println("End block is not walkable");
                                }
                                if (e.isStartNotSolid()) {
                                    PhaseBot.getConsole().println("Start block is not walkable");
                                }
                            }
                        }
                    }
                }
            }).start();
        }
    }

    @Override public String getCommand() {
        return "follow";
    }

    @Override public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
}
