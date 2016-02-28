package xyz.jadonfowler.phasebot.jna;

import com.sun.jna.*;

public interface RustInterface extends Library {
    
    void test();
    
    void addChunk(int x, int y, int z, int[][][] blocks);
}