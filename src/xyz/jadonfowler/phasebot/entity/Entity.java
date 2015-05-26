package xyz.jadonfowler.phasebot.entity;

import java.util.HashMap;

public class Entity {
	
	public static HashMap<Integer, Entity> entities = new HashMap<Integer, Entity>();
	
	public int entityId;
	public String type;
	public double x, y, z;
	public float yaw = 0, pitch = 0;
	
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
	
	public String toString(){
		return "Entity[entityId=" + entityId + ",type=" + type + ",x=" + x + ",y=" + y + ",z=" + z + "]";
	}
	
	public static Entity byId(int id){
		return entities.get(id);
	}
	
}