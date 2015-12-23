package xyz.jadonfowler.phasebot.world.material;

import java.util.*;
import lombok.*;

@ToString public class ItemType extends Material {
    
    @Getter long stackSize;
    @Getter List<ItemVariation> variations;

    @Builder public ItemType(long id, String displayName, String name, long stackSize, List<ItemVariation> variations) {
        super();
        this.id = id;
        this.displayName = displayName;
        this.name = name;
        this.stackSize = stackSize;
        this.variations = variations;
    }

    @AllArgsConstructor class ItemVariation {
        
        int metadata;
        String displayName;
    }
}
