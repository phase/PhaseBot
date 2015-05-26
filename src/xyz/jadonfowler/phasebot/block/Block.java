package xyz.jadonfowler.phasebot.block;

import org.spacehq.mc.protocol.data.game.Chunk;

import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.util.Vector3d;

public class Block {

	public Chunk chunk;
	public Vector3d pos;
	public Material m;
	
	public Block(double ax, double ay, double az){
		this(new Vector3d((int) Math.floor(ax), (int) Math.floor(ay), (int) Math.floor(az)));
	}
	
	public Block(Vector3d p) {
		this.chunk = getChunk();
		this.pos = p;
		Vector3d c = toChunkCoords();
		m = Material.getMaterial(getChunk().getBlocks().getBlock((int) Math.floor(c.x), (int) Math.floor(c.y), (int) Math.floor(c.z)));
		if(m == null)
			m = Material.AIR;
	}

	public Vector3d toChunkCoords() {
		return new Vector3d(pos.x / 16, pos.y / 16, pos.z / 16);
	}

	public Chunk getChunk() {
		Vector3d c = toChunkCoords();
		return PhaseBot.getBot().chunks
				[(int) Math.floor(c.x)]
				[(int) Math.floor(c.z)]
				[(int) Math.floor(c.y)];
	}
	
	public Material getMaterial(){
		return m;
	}

}
