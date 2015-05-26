package xyz.jadonfowler.phasebot.util;

import org.spacehq.mc.protocol.data.game.Position;

public class Vector3d {

	public double x, y, z;

	public Vector3d(double x, double y, double z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public String toString(){
		return "Vector[x=" + x + ",y=" + y + ",z=" + z + "]";
	}
	
	public static Vector3d fromPosition(Position p){
		return new Vector3d(p.getX(), p.getY(), p.getZ());
	}
	
	public static Position toPosition(Vector3d d){
		return new Position((int)Math.floor(d.x), (int)Math.floor(d.y), (int)Math.floor(d.z));
	}
}
