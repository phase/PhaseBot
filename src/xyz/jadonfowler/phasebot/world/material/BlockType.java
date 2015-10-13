package xyz.jadonfowler.phasebot.world.material;

import java.util.*;
import lombok.*;

@ToString public class BlockType extends Material {

    @Getter double hardness;
    @Getter long stackSize;
    @Getter boolean diggable;
    @Getter String boundingBox;
    @Getter String material = null;
    @Getter HashMap<String, Boolean> harvestTools;
    @Getter List<BlockVariation> variations;
    @Getter List<BlockDrop> drops;

    @Builder public BlockType(long id, String displayName, String name, double hardness, long stackSize,
            boolean diggable, String boundingBox, String material, HashMap<String, Boolean> harvestTools,
            List<BlockVariation> variations, List<BlockDrop> drops) {
        super();
        this.id = id;
        this.displayName = displayName;
        this.name = name;
        this.hardness = hardness;
        this.stackSize = stackSize;
        this.diggable = diggable;
        this.boundingBox = boundingBox;
        this.material = material;
        this.harvestTools = harvestTools;
        this.variations = variations;
        this.drops = drops;
    }

    @AllArgsConstructor class BlockVariation {

        int metadata;
        String displayName;
    }

    @AllArgsConstructor @RequiredArgsConstructor class BlockDrop {

        int id;
        int metadata = 0;
    }

    public boolean isSolid() {
        if (boundingBox == null) return false;
        if (boundingBox.equals("block")) return true;
        return false;
    }
}
