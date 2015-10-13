package xyz.jadonfowler.phasebot.world.material;

import java.util.*;
import lombok.*;
import xyz.jadonfowler.phasebot.*;

public class Material {

    @Getter private static final ArrayList<Material> materialsCache = new ArrayList<Material>();
    @Getter long id;
    @Getter String displayName;
    @Getter String name;

    public Material() {
        materialsCache.add(this);
    }

    public BlockType toBlock() {
        if (this instanceof BlockType) return (BlockType) this;
        return null;
    }

    public ItemType toItem() {
        if (this instanceof ItemType) return (ItemType) this;
        return null;
    }

    public static BlockType getBlock(String name) {
        for (Material m : materialsCache) {
            if (m instanceof BlockType) {
                BlockType b = (BlockType) m;
                if (b.getName().equalsIgnoreCase(name)) return b;
            }
        }
        return null;
    }

    public static ItemType getItem(String name) {
        for (Material m : materialsCache) {
            if (m instanceof ItemType) {
                ItemType i = (ItemType) m;
                if (i.getName().equalsIgnoreCase(name)) return i;
            }
        }
        return null;
    }

    public static Material getMaterial(int id) {
        for (Material m : materialsCache) {
            if (m.getId() == id) {
                return m;
            }
        }
        return null;
    }

    public static Material getMaterial(String s) {
        for (Material m : materialsCache) {
            if (m.getName().equalsIgnoreCase(s)) return m;
        }
        return null;
    }

    public static Material[] getMaterials(String... s) {
        Material[] materials = new Material[s.length];
        for (int i = 0; i < s.length; i++)
            materials[i] = getMaterial(s[i]);
        return materials;
    }
}