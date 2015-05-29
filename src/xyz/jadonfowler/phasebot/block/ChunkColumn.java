package xyz.jadonfowler.phasebot.block;

import org.spacehq.mc.protocol.data.game.Chunk;

public class ChunkColumn {
  
  static ArrayList<ChunkColumn> chunks = new ArrayList<ChunkColumn>();

  public Chunk[] y;
  public int x, z;

  public ChunkColumn(int x, int z, Chunk[] y){
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public static Chunk getChunk(Vector3d v){
    return getChunk((int) Math.floor(v.x), (int) Math.floor(v.y), (int) Math.floor(v.z));
  }

  public static Chunk getChunk(Position p){
    return getChunk(p.getX(), p.getY(), p.getZ());
  }

  public static Chunk getChunk(int x, int y, int z){
    for(ChunkColumn cl : chunks){
      if(cl.x == x && cl.z == z){
        return cl.y[y];
      }
    }
    return null;
  }

}
