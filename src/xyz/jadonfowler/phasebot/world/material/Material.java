package xyz.jadonfowler.phasebot.world.material;

import java.util.*;
import lombok.*;

public class Material {

    @Getter private static final ArrayList<Material> materials = new ArrayList<Material>();

    public Material() {
        materials.add(this);
    }

    public static BlockType getBlock(String name) {
        for (Material m : materials) {
            if (m instanceof BlockType) {
                BlockType b = (BlockType) m;
                if (b.getName().equalsIgnoreCase(name)) return b;
            }
        }
        return null;
    }

    public static ItemType getItem(String name) {
        for (Material m : materials) {
            if (m instanceof ItemType) {
                ItemType i = (ItemType) m;
                if (i.getName().equalsIgnoreCase(name)) return i;
            }
        }
        return null;
    }
}
