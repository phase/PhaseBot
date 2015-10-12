package xyz.jadonfowler.phasebot.world.material;

import java.util.*;
import lombok.*;

@Builder @ToString public class BlockType extends Material {

    @Getter long id;
    @Getter String displayName;
    @Getter String name;
    @Getter double hardness;
    @Getter long stackSize;
    @Getter boolean diggable;
    @Getter String boundingBox;
    @Getter String material;
    @Getter HashMap<String, Boolean> harvestTools;
    @Getter List<BlockVariation> variations;
    @Getter List<BlockDrop> drops;

    @AllArgsConstructor class BlockVariation {

        int metadata;
        String displayName;
    }

    @AllArgsConstructor @RequiredArgsConstructor class BlockDrop {

        int id;
        int metadata = 0;
    }
}
