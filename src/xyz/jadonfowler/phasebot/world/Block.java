package xyz.jadonfowler.phasebot.world;

import java.util.*;
import lombok.*;
import org.spacehq.mc.protocol.data.game.*;
import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.util.*;
import xyz.jadonfowler.phasebot.world.material.*;

public class Block {
    @Getter @NonNull public Chunk chunk;
    @Getter public Vector3d pos;
    @Getter public BlockType material;
    int id;
    @Getter private static ArrayList<Block> cache = new ArrayList<Block>();

    private Block(Position p) {
        this(Vector3d.fromPosition(p));
    }
    
    private Block(double ax, double ay, double az) {
        this(new Vector3d(ax, ay, az).floor().round());
    }

    private Block(Vector3d p) {
        this.pos = p;
        this.chunk = ChunkColumn.getChunk(this);
        Vector3d b = new Vector3d(
                pos.x > 0 ? pos.x % 16 : 16 - (Math.abs(pos.x) % 16),
                pos.y > 0 ? pos.y % 16 : 16 - (Math.abs(pos.y) % 16),
                pos.z > 0 ? pos.z % 16 : 16 - (Math.abs(pos.z) % 16))
                .floor();
        PhaseBot.getConsole().log("chunk is null: " + chunk == null);
        int id = 0;
        try {
            id = chunk
                    .getBlocks()
                    .getBlock((int) b.x, (int) b.y, (int) b.z);
        }
        catch (Exception e) {
            e.printStackTrace();
            id = 0;
        }
        this.id = id;
        material = Material.getMaterial(id).toBlock();
        cache.add(this);
    }

    public Vector3d toChunkCoords() {
        return new Vector3d(pos.x / 16, pos.y / 16, pos.z / 16).floor().round();
    }

    public Block getRelative(int rx, int ry, int rz) {
        return new Block(pos.x + rx, pos.y + ry, pos.z + rz);
    }

    public int getTypeId() {
        return id;
    }

    public static Block getBlock(Vector3d p) {
        for (Block b : cache)
            if (b.getPos().equals(p)) return b;
        return new Block(p);
    }

    public static Block getBlock(double x, double y, double z) {
        PhaseBot.getConsole().log("getBlock(" + x + "," + y + "," + z + ")");
        return getBlock(new Vector3d(x, y, z));
    }

    public static Block getBlock(double x, double z) {
        int y = 256; // World Height
        while (getBlock(x, y--, z).getMaterial().getId() == 0);
        return getBlock(x, y, z);
    }
}