package xyz.jadonfowler.phasebot.block;

public class ChunkList {
  
  static ArrayList<ChunkList> chunks = new ArrayList<ChunkList>();

  public Chunk[] y;
  public int x, z;

  public ChunkList(int x, int z, Chunk[] y){
    this.x = x;
    this.y = y;
    this.z = z;
  }

  public static Chunk getChunk(int x, int y, int z){
    for(ChunkList cl : chunks){
      if(cl.x == x && cl.z == z){
        return cl.y[y];
      }
    }
    return null;
  }

}
