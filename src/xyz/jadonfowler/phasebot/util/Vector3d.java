package xyz.jadonfowler.phasebot.util;

import org.spacehq.mc.protocol.data.game.Position;

import lombok.*;

@ToString
@AllArgsConstructor
public class Vector3d {

	@Getter public double x = 0.0d, y = 0.0d, z = 0.0d;

	public Vector3d floor(){
		this.x = Math.floor(x);
		this.y = Math.floor(y);
		this.z = Math.floor(z);
		return this;
	}

	public Vector3d ceil(){
		this.x = Math.ceil(x);
		this.y = Math.ceil(y);
		this.z = Math.ceil(z);
		return this;
	}

	public Vector3d int(){
		this.x = (int) x;
		this.y = (int) y;
		this.z = (int) z;
		return this;
	}

	public Vector3d clone(){
		return new Vector3d(this.x, this.y, this.z);
	}
	
	public static Vector3d fromPosition(@NotNull Position p){
		return new Vector3d(p.getX(), p.getY(), p.getZ());
	}
	
	public static Position toPosition(@NotNull Vector3d d){
		return new Position((int)Math.floor(d.x), (int)Math.floor(d.y), (int)Math.floor(d.z));
	}

	public static Vector3d fromString(@NotNull String s){
		//String format: "X Y Z"
		try{
			double x = Double.parseDouble(s.split(" ")[0]);
			double y = Double.parseDouble(s.split(" ")[1]);
			double z = Double.parseDouble(s.split(" ")[2]);
			return new Vector3d(x, y, z);
		catch(Exception e){
			throw new IllegalArgumentException(s);
			return null;
		}
	}

}
