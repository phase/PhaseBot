package xyz.jadonfowler.phasebot.util;

import org.spacehq.mc.protocol.data.game.Position;

public class Vector3d {

	public double x, y, z;

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String toString(){
		return "Vector[x=" + x + ",y=" + y + ",z=" + z + "]";
	}

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
	
	public static Vector3d fromPosition(Position p){
		return new Vector3d(p.getX(), p.getY(), p.getZ());
	}
	
	public static Position toPosition(Vector3d d){
		return new Position((int)Math.floor(d.x), (int)Math.floor(d.y), (int)Math.floor(d.z));
	}

}
