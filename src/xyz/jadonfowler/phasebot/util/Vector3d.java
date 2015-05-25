package xyz.jadonfowler.phasebot.util;

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
	
}
