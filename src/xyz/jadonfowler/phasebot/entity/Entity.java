package xyz.jadonfowler.phasebot.entity;

import java.util.HashMap;

import lombok.*;

@ToString(exclude={"pitch", "yaw"})
public class Entity {
	
	@Getter private static HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
	
	@Getter private int entityId;
	@Getter private String type;
	@Getter @Setter public double x, y, z;
	@Getter @Setter public float yaw = 0, pitch = 0;
	
	public Entity(int i, String type, double x, double y, double z, float yaw, float pitch){
		this.entityId = i;
		this.type = type;
		this.x = x;
		this.y = y;
		this.z = z;
		this.yaw = yaw;
		this.pitch = pitch;
		entities.put(entityId, this);
	}
	
	public Entity(int i, String type, double x, double y, double z){
		this(i, type, x, y, z, 0, 0);
	}
	
	public void remove(){
		entities.remove(this.entityId);
	}
	
	public static Entity byId(int id){
		return entities.get(id);
	}
	
}
