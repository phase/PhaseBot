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
		this.pos = p;
		this.chunk = getChunk();
		System.out.println("BLOCK VECTOR: " + p);
		int id = 
				getChunk()
				.getBlocks()
				.getBlock(
						(int) Math.floor(pos.x % 16), 
						(int) Math.floor(pos.y % 16), 
						(int) Math.floor(pos.z % 16));
		System.out.println("ID: " + id);
		m = Material.getMaterial(id);
	}

	public Vector3d toChunkCoords() {
		return 
				new Vector3d(
				pos.x / 16, 
				pos.y / 16, 
				pos.z / 16);
	}

	public Chunk getChunk() {
		Vector3d c = toChunkCoords();
		return PhaseBot.getBot().chunks
				[(int) Math.floor(c.x)]
				[(int) Math.floor(c.z)]
				[(int) Math.floor(c.y)+1];
	}
	
	public Material getMaterial(){
		return m;
	}

	public void printChunk(Chunk c){
		System.out.println(toChunkCoords());
		for(int x = 0; x < 16; x++){
			for(int y = 0; y < 16; y++){
				for(int z = 0; z < 16; z++){
					System.out.println(c.getBlocks().getBlock(x, y, z) + ": " + x + " " + y + " " + z);
				}
			}
		}
	}
	
}
