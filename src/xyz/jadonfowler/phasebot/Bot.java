package xyz.jadonfowler.phasebot;

import java.net.*;
import java.util.*;
import lombok.*;
import org.spacehq.mc.protocol.data.game.*;
import org.spacehq.mc.protocol.data.game.values.*;
import org.spacehq.mc.protocol.data.game.values.entity.player.*;
import org.spacehq.mc.protocol.packet.ingame.client.*;
import org.spacehq.mc.protocol.packet.ingame.client.player.*;
import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.entity.*;
import xyz.jadonfowler.phasebot.inventory.*;
import xyz.jadonfowler.phasebot.pathfind.*;
import xyz.jadonfowler.phasebot.util.*;
import xyz.jadonfowler.phasebot.world.*;
import xyz.jadonfowler.phasebot.world.material.*;

public class Bot {

    @Getter @Setter private String username;
    @Getter @Setter private String password;
    @Getter @Setter private String host;
    @Getter @Setter private int port;
    @Getter @Setter private Proxy proxy = Proxy.NO_PROXY;
    @Getter public Vector3d pos;
    @Getter public float pitch = 0;
    @Getter public float yaw = 0;
    @Getter public int entityId = 0;
    public boolean isDerp = false;
    @Getter @Setter private boolean interuptMoveAlong = false;
    @Getter @Setter private Client client;
    @Getter @Setter public Inventory inventory;
    public Vector3d[] positions; // Do we still need this?
    // Variables
    @Getter private HashMap<String, String> variables = new HashMap<String, String>();

    public Bot(String username, String password, String host, int port, Proxy proxy) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.proxy = proxy;
        this.positions = new Vector3d[4];
        this.pos = new Vector3d(0, 0, 0);
        variables.put("host", host);
    }

    public void runCommand(final String s, boolean newThread) {
        if (newThread) {
            new Thread(new Runnable() {

                public void run() {
                    if (client.getSession() != null && client.getSession().isConnected())
                        PhaseBot.getCommandManager().performCommand(s, s.split(" "), client.getSession());
                }
            }).start();
        }
        else {
            if (client.getSession() != null && client.getSession().isConnected())
                PhaseBot.getCommandManager().performCommand(s, s.split(" "), client.getSession());
        }
    }

    public void runCommand(String s) {
        runCommand(s, false);
    }

    public void derp(final Session s) {
        new Thread(new Runnable() {

            public void run() {
                while (isDerp) {
                    look((PhaseBot.random.nextFloat() * 10000) % 180, ((PhaseBot.random.nextFloat() * 10000) % 180));
                    swing();
                    // jump(0, 1, 0);
                    try {
                        Thread.sleep(40);
                    }
                    catch (InterruptedException e) {}
                }
            }
        }).start();
    }

    public void look(float yaw, float pitch) {
        client.getSession().send(new ClientPlayerPositionRotationPacket(false, pos.x, pos.y, pos.z, yaw, pitch));
    }

    public void swing() {
        client.getSession().send(new ClientSwingArmPacket());
    }

    public void centerPosition() {
        // return;
        double dx = pos.x > 0 ? Math.floor(pos.x) + 0.5d : Math.round(pos.x) - 0.5d;
        double dy = Math.floor(pos.y);
        double dz = pos.z > 0 ? Math.floor(pos.z) + 0.5d : Math.round(pos.z) - 0.5d;
        client.getSession().send(new ClientPlayerPositionRotationPacket(false, dx, dy, dz, yaw, pitch));
    }

    public void fall() {
        while (Block.getBlock(pos.x, pos.y - 1, pos.z).getMaterial() == Material.getMaterial(0))
            move(0, -1, 0);
    }

    public void jump(double x, double y, double z) {
        if (y > 0) move(0, Math.abs(y), 0);
        move(x, 0, z);
        fall();
        centerPosition();
    }

    public void moveTo(Entity e) {
        moveTo((int) Math.floor(e.x), (int) Math.floor(e.y), (int) Math.floor(e.z));
    }

    public void moveTo(double tx, double ty, double tz) {
        moveTo(new Vector3d(tx, ty, tz));
    }

    public void moveTo(Vector3d to) {
        Vector3d v = new Vector3d(to.x - pos.x, to.y - pos.y, to.z - pos.z).floor();
        jump(v.x, v.y, v.z);
    }

    public boolean pathing = false;

    public void moveAlong(ArrayList<Tile> tiles) {
        interuptMoveAlong = false;
        pathing = true;
        final Iterator<Tile> itr = tiles.iterator();
        final Vector3d start = pos.clone();
        itr.next();
        new Thread(new Runnable() {

            public void run() {
                try {
                    while (itr.hasNext()) {
                        if (interuptMoveAlong) {
                            PhaseBot.getConsole().println("Block changed! Rerouting path...");
                            PhaseBot.getCommandManager().performLastCommand();
                            interuptMoveAlong = false;
                            return;
                        }
                        Tile t = itr.next();
                        Vector3d v = t.getLocation(start.clone()).clone().add(new Vector3d(0.5, 0, 0.5));
                        moveTo(v);
                    }
                    pathing = false;
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void moveAlongArc(double rx, double ry, double rz) {
        // Get absolute cords
        final double tx = pos.x - rx;
        final double ty = pos.y - ry;
        final double tz = pos.z - rz;
        final double fx = pos.x;
        final double fy = pos.y;
        final double fz = pos.z;
        int[] angles = { 30, 45, 60 };
        for (int t : angles) {
            /* @formatter:off
             * rz = sin(to.z > from.z ? -t : t) * (to.z - from.z) 
             * rx = cos(to.x > from.x ? -t : t) * (to.x - from.x) 
             * ry = tan(to.y < from.y ? -t : t) * (to.y - from.y)
             * @formatter:on
             */
            double arx = Math.cos(tx > fx ? -t : t) * (tx - fx);
            double ary = Math.tan(ty < fy ? -t : t) * (ty - fy);
            double arz = Math.sin(tz > fz ? -t : t) * (tz - fz);
            // PhaseBot.getConsole().println("ARC: " + arx + " " + ary + " " +
            // arz);
            move(arx, ary, arz);
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        moveTo(tx, ty, tz);
    }

    public void move(double rx, double ry, double rz) {
        if (isDiagonal(rx, ry, rz)) {
            moveDiagonal(rx, ry, rz);
            return;
        }
        // PhaseBot.getConsole().println("m: " + rx + " " + ry + " " + rz);
        double l = (pos.x + rx) - pos.x;
        double w = (pos.z + rz) - pos.z;
        double h = (pos.y + ry) - pos.y;
        double c = Math.sqrt(l * l + w * w);
        double a1 = -Math.asin(l / c) / Math.PI * 180;
        double a2 = Math.acos(w / c) / Math.PI * 180;
        if (a2 > 90) yaw = (float) (180 - a1);
        else yaw = (float) a1;
        if (rx == 0 && rz == 0) yaw = 0;
        pitch = (float) Math.atan((h / c));
        // PhaseBot.getConsole().println("p: " + pitch + " y:" + yaw);
        int numberOfSteps = (int) ((int) 2.0
                * Math.floor(Math.sqrt(Math.pow(rx, 2) + Math.pow(ry, 2) + Math.pow(rz, 2))));
        double sx = rx / numberOfSteps;
        double sy = ry / numberOfSteps;
        double sz = rz / numberOfSteps;
        // PhaseBot.getConsole().println("s: " + sx + " " + sy + " " + sz + " :
        // " +
        // numberOfSteps);
        for (int i = 0; i < numberOfSteps; i++) {
            pos.x += sx;
            pos.y += sy;
            pos.z += sz;
            // PhaseBot.getConsole().println("Moving " + pos);
            client.getSession().send(new ClientPlayerPositionRotationPacket(false, pos.x, pos.y, pos.z, yaw, pitch));
            try {
                Thread.sleep(100);
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        centerPosition();
    }

    private void moveDiagonal(double x, double y, double z) {
        pos.x += x;
        pos.y += y;
        pos.z += z;
        centerPosition();
        try {
            Thread.sleep(0);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private boolean isDiagonal(double x, double y, double z) {
        if ((x == 1 && z == 1 && y == 0) || (x == 1 && z == -1 && y == 0) || (x == -1 && z == 1 && y == 0)
                || (x == -1 && z == -1 && y == 0))
            return true;
        return false;
    }

    public void breakBlockAbsolute(Vector3d a) {
        Vector3d r = absoluteToRelative(a);
        breakBlock(r.x, r.y, r.z);
    }

    public void breakBlock(double rx, double ry, double rz) {
        final Position p = new Position((int) Math.floor(pos.x + rx), (int) Math.floor(pos.y + ry),
                (int) Math.floor(pos.z + rz));
        // PhaseBot.getConsole().println("Digging at: " + p.getX() + " " +
        // p.getY() + " " + p.getZ());
        client.getSession().send(new ClientPlayerActionPacket(PlayerAction.START_DIGGING, p, Face.TOP));
        swing();
        client.getSession().send(new ClientPlayerActionPacket(PlayerAction.FINISH_DIGGING, p, Face.TOP));
    }

    public void say(String s) {
        client.getSession().send(new ClientChatPacket(s));
    }

    public Vector3d relativeToAbsolute(Vector3d d) {
        return new Vector3d(pos.x + d.x, pos.y + d.y, pos.z + d.z);
    }

    public Vector3d absoluteToRelative(Vector3d a) {
        return new Vector3d(pos.x - a.x, pos.y - a.y, pos.z - a.z);
    }

    public Face getPlaceFace(Vector3d d) {
        for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    Block b = Block.getBlock(new Vector3d(x + d.x, y + d.y, z + d.z));
                    if (b.getMaterial() != Material.getMaterial(0)) {
                        if ((x == -1 && z == 0 && y == 0)) return Face.WEST;
                        else if ((x == 1 && z == 0 && y == 0)) return Face.EAST;
                        else if ((x == 0 && z == 0 && y == 1)) return Face.BOTTOM;
                        else if ((x == 0 && z == 0 && y == -1)) return Face.TOP;
                        else if ((x == 0 && z == 1 && y == 0)) return Face.SOUTH;
                        else if ((x == 0 && z == -1 && y == 0)) return Face.NORTH;
                    }
                }
            }
        }
        return Face.INVALID;
    }

    public void placeBlockAbsolute(Vector3d location) {
        placeBlock(absoluteToRelative(location));
    }

    public void placeBlock(Vector3d location) {
        Face face = Face.INVALID;
        Vector3d blockLocation = null;
        float cursorX = 0, cursorY = 0, cursorZ = 0;
        getBlock: for (int x = -1; x < 2; x++) {
            for (int y = -1; y < 2; y++) {
                for (int z = -1; z < 2; z++) {
                    Block b = Block.getBlock(new Vector3d(x + location.x, y + location.y, z + location.z));
                    if (b.getMaterial() != Material.getMaterial(0)) {
                        if ((x == 0 && z == 0 && y == -1)) {
                            face = Face.TOP;
                            blockLocation = b.getPos();
                            break getBlock;
                        }
                        else if ((x == -1 && z == 0 && y == 0)) {
                            face = Face.SOUTH;
                            blockLocation = b.getPos();
                            break getBlock;
                        }
                        else if ((x == 1 && z == 0 && y == 0)) {
                            face = Face.NORTH;
                            blockLocation = b.getPos();
                            break getBlock;
                        }
                        else if ((x == 0 && z == 0 && y == 1)) {
                            face = Face.BOTTOM;
                            blockLocation = b.getPos();
                            break getBlock;
                        }
                        else if ((x == 0 && z == 1 && y == 0)) {
                            face = Face.EAST;
                            blockLocation = b.getPos();
                            break getBlock;
                        }
                        else if ((x == 0 && z == -1 && y == 0)) {
                            face = Face.WEST;
                            blockLocation = b.getPos();
                            break getBlock;
                        }
                    }
                }
            }
        }
        if (face == Face.INVALID) {
            PhaseBot.getConsole().println("Block cannot be place at " + location + ".");
            return;
        }
        // $20 says this doesn't matter, thanks Mojang
        switch (face) {
        case TOP:
            look(0, 90);
            // cursorX = 8;
            // cursorZ = 8;
            cursorX = 0;
            cursorY = 0;
            cursorZ = 0;
            break;
        case BOTTOM:
            look(0, -90);
            cursorX = 8;
            cursorY = 16;
            cursorZ = 8;
            break;
        case NORTH:
            look(0, 0);
            cursorX = 8;
            cursorY = 8;
            break;
        case SOUTH:
            look(180, 0);
            cursorX = 8;
            cursorY = 8;
            cursorZ = 16;
            break;
        case EAST:
            look(90, 0);
            cursorX = 16;
            cursorY = 8;
            cursorZ = 8;
            break;
        case WEST:
            look(-90, 0);
            cursorX = 8;
            cursorZ = 0;
            break;
        case INVALID:
        default:
            return;
        }
        blockLocation = blockLocation.floor();
        // PhaseBot.getConsole().println(face + " " + cursorX + " " + cursorY +
        // " " + cursorZ + " :: " + blockLocation);
        client.getSession().send(new ClientPlayerPlaceBlockPacket(Vector3d.toPosition(blockLocation), face,
                inventory.getHeldItem(), cursorX, cursorY, cursorZ));
        try {
            if (Material.getMaterial(inventory.getHeldItem().getId()) instanceof BlockType)
                ChunkColumn.setBlock(Vector3d.toPosition(location), inventory.getHeldItem().getId());
        }
        catch (Exception e) {
            // Silently catch because that's good software design
        }
    }

    public void setSlot(int i) {
        // PhaseBot.getConsole().println("Changing slot to " + i);
        client.getSession().send(new ClientChangeHeldItemPacket(i));
        inventory.setHeldSlot(i);
        // Fucking Mojang is fucking stupid because they don't update a
        // Player's slot until they move. THANKS DUMBASSES
        look(yaw, pitch);
    }

    public void getCloseToAbsolute(Vector3d a) {
        getCloseTo(absoluteToRelative(a));
    }

    public void getCloseTo(Vector3d r) {
        double tx = pos.x + r.x;
        double ty = pos.y + r.y;
        double tz = pos.z + r.z;
        double distance = Math.sqrt(Math.pow(pos.x - tx, 2) + Math.pow(pos.y - ty, 2) + Math.pow(pos.z - tz, 2));
        if (distance > 4) {
            move(r.x, r.y, r.z);
            /*
             * @formatter:off
             * try {
                System.out.println(tx + " " + ty + " " + tz);
                AStar path = new AStar(new Vector3d(pos.x, pos.y, pos.z), new Vector3d(tx, ty, tz), 100);
                ArrayList<Tile> route = path.iterate();
                PathingResult result = path.getPathingResult();
                switch (result) {
                case SUCCESS:
                    // Path was successful. Do something here.
                    PhaseBot.getBot().moveAlong(route);
                    break;
                case NO_PATH:
                default:
                    break;
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            @formatter:on
            */
        }
    }

    public void openChest() {
        // client.getSession().send(new Client);
    }

    public double getRelativeDistanceAway(Vector3d r) {
        return getDistanceAway(relativeToAbsolute(r));
    }

    public double getDistanceAway(Vector3d l) {
        return Math.sqrt(Math.pow(pos.x - l.x, 2) + Math.pow(pos.y - l.y, 2) + Math.pow(pos.z - l.z, 2));
    }

    public Block getClosestBlock(Material material, int range) {
        ArrayList<Block> matches = new ArrayList<Block>();
        for (int x = 0; x < range; x++) {
            for (int y = 0; y < range; y++) {
                for (int z = 0; z < range; z++) {
                    Block b; // cache
                    if ((b = Block.getBlock(pos.x + x, pos.y + x, pos.z + z)).getMaterial() == material) {
                        matches.add(b);
                    }
                }
            }
        }
        double shortestDistance = range;
        Block best = null;
        for (Block g : matches) {
            if (shortestDistance == range) {
                best = g;
                shortestDistance = getDistanceAway(g.getPos());
            }
            else {
                double d; // cache
                if ((d = getDistanceAway(g.getPos())) < shortestDistance) {
                    best = g;
                    shortestDistance = d;
                }
            }
        }
        System.out.println(best.getPos());
        return best;
    }
}