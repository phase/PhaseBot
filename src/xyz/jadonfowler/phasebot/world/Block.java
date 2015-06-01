package xyz.jadonfowler.phasebot.world;

import lombok.*;
import org.spacehq.mc.protocol.data.game.*;
import xyz.jadonfowler.phasebot.util.*;

public class Block {

	@Getter @NonNull
	public Chunk chunk;

	@Getter
	public Vector3d pos;

	@Getter(AccessLevel.PUBLIC)
	public Material material;

	public Block(Position p) {
		this(Vector3d.fromPosition(p));
	}

	public Block(double ax, double ay, double az) {
		this(new Vector3d(ax, ay, az).floor().round());
	}

	public Block(Vector3d p) {
		this.pos = p;
		this.chunk = ChunkColumn.getChunk(this);
		Vector3d b = new Vector3d(pos.x % 16, pos.y % 16, pos.z % 16);
		System.out.println(toChunkCoords() + " : " + b);
		ChunkColumn.printChunk(chunk);
		int id = 0;
		try {
			id = 
					chunk
					.getBlocks()
					.getBlock(
							(int) b.x, (int) b.y, (int) b.z);
		} catch (Exception e) {
			e.printStackTrace();
		}
		material = Material.getMaterial(id);
	}

	public Vector3d toChunkCoords() {
		return new Vector3d(pos.x / 16, pos.y / 16, pos.z / 16).floor().round();
	}

}
