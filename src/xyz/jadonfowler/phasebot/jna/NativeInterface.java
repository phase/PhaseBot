package xyz.jadonfowler.phasebot.jna;

import com.sun.jna.*;

public interface NativeInterface extends Library {
    void add_chunk(int x, int y, int z, int[][][] blocks);

    void update_block(int x, int y, int z, int id);
}