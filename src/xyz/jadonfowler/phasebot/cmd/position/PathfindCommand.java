package xyz.jadonfowler.phasebot.cmd.position;

import java.util.*;
import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.entity.*;
import xyz.jadonfowler.phasebot.pathfind.*;
import xyz.jadonfowler.phasebot.pathfind.AStar.InvalidPathException;
import xyz.jadonfowler.phasebot.util.*;

public class PathfindCommand extends Command {

    @Override public void exec(String in, String[] args, Session s) {
        try {
            Vector3d start = PhaseBot.getBot().getPos().clone().addY(-1);
            Vector3d end = null;
            if (args.length == 2) {
                if (args[1].matches("[-+]?\\d*\\.?\\d+")) {
                    int id = Integer.parseInt(args[1]);
                    Entity e = Entity.byId(id);
                    end = new Vector3d(e.getX(), e.getY()-1, e.getZ());
                }
                else {
                    try {
                        UUIDFetcher uf = new UUIDFetcher(Arrays.asList(args[1]));
                        UUID u = uf.call().get(args[1]);
                        Player e = Player.byUUID(u);
                        end = new Vector3d(e.getX(), e.getY()-1, e.getZ());
                        System.out.println("I'm moving towards " + args[1] + " at " + end.clone().floor());
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            // get our vars
            if (end == null) {
                double x = Double.parseDouble(args[1]);
                double y = Double.parseDouble(args[2]);
                double z = Double.parseDouble(args[3]);
                end = PhaseBot.getBot().relativeToAbsolute(new Vector3d(x, y, z)).clone().addY(-1);
            }
            System.out.println("start= " + start + " \nend= " + end);
            // create our pathfinder
            AStar path = new AStar(start, end, 100);
            // get the list of nodes to walk to as a Tile object
            ArrayList<Tile> route = path.iterate();
            // get the result of the path trace
            PathingResult result = path.getPathingResult();
            switch (result) {
            case SUCCESS:
                // Path was successful. Do something here.
                PhaseBot.getBot().moveAlong(route);
                System.out.println("Path was found! :D");
                break;
            case NO_PATH:
                // No path found, throw error.
                System.out.println("No path found!");
                break;
            default:
                break;
            }
        }
        catch (InvalidPathException e) {
            // InvalidPathException will be thrown if start or end block is air
            if (e.isEndNotSolid()) {
                System.out.println("End block is not walkable");
            }
            if (e.isStartNotSolid()) {
                System.out.println("Start block is not walkable");
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override public String getCommand() {
        return "pathfind";
    }

    @Override public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }
}
