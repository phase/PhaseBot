package xyz.jadonfowler.phasebot.world;

import java.util.*;
import org.spacehq.mc.protocol.data.game.*;
import xyz.jadonfowler.phasebot.util.*;

public class ChunkColumn {

	static ArrayList<ChunkColumn> chunks = new ArrayList<ChunkColumn>();

	public Chunk[] y;

	public int x, z;

	public ChunkColumn(int x, int z, Chunk[] y) {
		this.x = x;
		this.z = z;
		for (Chunk c : y) {
			if (x == 55 && z == 39)
				if (c == null || c.getBlocks() == null){
					c = new Chunk(true);
				}
		}
		this.y = y;
		chunks.add(this);
	}

	public static Chunk getChunk(Vector3d v) {
		return getChunk(Vector3d.toPosition(v));
	}

	public static Chunk getChunk(Position p) {
		return getChunk(p.getX(), p.getY(), p.getZ());
	}

	public static Chunk getChunk(Block b) {
		return getChunk(b.toChunkCoords());
	}

	public static Chunk getChunk(int x, int y, int z) {
		for (ChunkColumn cl : chunks)
			if (cl.x == x && cl.z == z) {
				if(cl.y[y].getBlocks() == null) continue;
				return cl.y[y];
			}
		return null;
	}

	public static void setBlock(Position p, int id) {
		Chunk c = getChunk((int) Math.floor(p.getX() / 16), (int) Math.floor(p.getY() / 16),
				(int) Math.floor(p.getZ() / 16));
		c.getBlocks().setBlock((int) Math.floor(p.getX() % 16), (int) Math.floor(p.getY() % 16),
				(int) Math.floor(p.getZ() % 16), id);
	}

	public static void printChunk(Chunk c) {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				for (int z = 0; z < 16; z++) {
					System.out.println(c.getBlocks().getBlock(x, y, z) + ": " + x + " " + y + " " + z);
				}
			}
		}
	}
}
