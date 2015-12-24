package xyz.jadonfowler.phasebot.world;

import java.util.*;
import org.spacehq.mc.protocol.data.game.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.util.*;

public class ChunkColumn {

	static ArrayList<ChunkColumn> chunks = new ArrayList<ChunkColumn>();

	public Chunk[] y;

	public int x, z;

	public ChunkColumn(int x, int z, Chunk[] y) {
		this.x = x;
		this.z = z;
		for (Chunk c : y) {
			if (c == null || c.getBlocks() == null) {
				c = new Chunk(true);
				c.getBlocks().fill(0);
			} else if (x == 61 && z == 38) {
				String blocks = "";
				for (short b : c.getBlocks().getData()) {
					blocks += b + " ";
				}
				PhaseBot.getConsole().log(" new chunk: " + blocks);
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
		return getChunk(b.toChunkCoords().toPosition());
	}

	public static Chunk getChunk(int x, int y, int z) {
		y--;
		for (ChunkColumn cl : chunks)
			if (cl.x == x && cl.z == z) {
				if (cl.y[y] == null || cl.y[y].getBlocks() == null)
					continue;
				return cl.y[y];
			}
		return null;
	}

	public static void setBlock(Position p, int id) {
		Vector3d v = new Vector3d(p.getX() > 0 ? Math.floor(p.getX() / 16) : -1 + Math.ceil(p.getX() / 16),
				p.getY() > 0 ? Math.floor(p.getY() / 16) : -1 + Math.ceil(p.getY() / 16),
				p.getZ() > 0 ? Math.floor(p.getZ() / 16) : -1 + Math.ceil(p.getZ() / 16));
		Chunk c = getChunk((int) v.x, (int) v.y, (int) v.z);
		Vector3d b = new Vector3d(Math.floor(p.getX() > 0 ? p.getX() % 16 : 16 - (Math.abs(p.getX()) % 16)),
				Math.floor(p.getY() > 0 ? p.getY() % 16 : 16 - (Math.abs(p.getY()) % 16)),
				Math.floor(p.getZ() > 0 ? p.getZ() % 16 : 16 - (Math.abs(p.getZ()) % 16)));
		if (c == null || c.getBlocks() == null) {
			c = new Chunk(false);
			c.getBlocks().fill(0);
		}
		c.getBlocks().setBlock((int) b.x, (int) b.y, (int) b.z, id);
	}

	public static void printChunk(Chunk c) {
		for (int x = 0; x < 16; x++) {
			for (int y = 0; y < 16; y++) {
				for (int z = 0; z < 16; z++) {
					PhaseBot.getConsole().log(c.getBlocks().getBlock(x, y, z) + ": " + x + " " + y + " " + z);
				}
			}
		}
	}
}
