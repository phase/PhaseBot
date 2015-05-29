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
		return new Vector3d(Math.floor(x), Math.floor(y), Math.floor(z));
	}

	public Vector3d ceil(){
		return new Vector3d(Math.ceil(x), Math.ceil(y), Math.ceil(z));
	}

	public Vector3d int(){
		return new Vector3d((int) x, (int) y, (int) z);
	}
	
	public static Vector3d fromPosition(Position p){
		return new Vector3d(p.getX(), p.getY(), p.getZ());
	}
	
	public static Position toPosition(Vector3d d){
		return new Position((int)Math.floor(d.x), (int)Math.floor(d.y), (int)Math.floor(d.z));
	}

}
