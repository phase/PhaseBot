package xyz.jadonfowler.phasebot.world.material;

import java.util.*;
import lombok.*;

@Builder @ToString public class ItemType extends Material {

    @Getter long id;
    @Getter String displayName;
    @Getter String name;
    @Getter long stackSize;
    @Getter List<ItemVariation> variations;

    @AllArgsConstructor class ItemVariation {

        int metadata;
        String displayName;
    }
}
