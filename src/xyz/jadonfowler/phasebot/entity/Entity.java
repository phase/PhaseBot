package xyz.jadonfowler.phasebot.entity;

import java.util.*;
import lombok.*;
import xyz.jadonfowler.phasebot.util.*;

@ToString(exclude = { "pitch", "yaw" }) public class Entity {

    @Getter(AccessLevel.PUBLIC) private static HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();

    @Getter(AccessLevel.PUBLIC) private int entityId;

    @Getter(AccessLevel.PUBLIC) private String type;

    @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) public double x, y, z;

    @Getter(AccessLevel.PUBLIC) @Setter(AccessLevel.PUBLIC) public float yaw = 0, pitch = 0;

    public Entity(int i, String type, double x, double y, double z, float yaw, float pitch) {
        this.entityId = i;
        this.type = type;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        entities.put(entityId, this);
    }

    public Entity(int i, String type, double x, double y, double z) {
        this(i, type, x, y, z, 0, 0);
    }

    public void remove() {
        entities.remove(this.entityId);
    }

    public static Entity byId(int id) {
        return entities.get(id);
    }
    
    public Vector3d getPos(){
        return new Vector3d(x, y, z);
    }
}
