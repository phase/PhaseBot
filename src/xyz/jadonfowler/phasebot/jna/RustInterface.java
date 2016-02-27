package xyz.jadonfowler.phasebot.jna;

import com.sun.jna.*;

public interface RustInterface extends Library {
    String JNA_LIBRARY_NAME = "phasebot";
    NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(JNA_LIBRARY_NAME);
    
    RustInterface INSTANCE = (RustInterface) Native.loadLibrary(JNA_LIBRARY_NAME, RustInterface.class);
    
    void addChunk(int x, int y, int z, int[][][] blocks);
}