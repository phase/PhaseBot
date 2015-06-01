package xyz.jadonfowler.phasebot.util;

import lombok.*;
import org.spacehq.mc.protocol.data.game.*;

public class Vector3d {

	@Getter
	@Setter
	public double x;

	@Getter
	@Setter
	public double y;

	@Getter
	@Setter
	public double z;

	public Vector3d(double x, double y, double z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public Vector3d floor() {
		this.x = Math.floor(x);
		this.y = Math.floor(y);
		this.z = Math.floor(z);
		return this;
	}

	public Vector3d ceil() {
		this.x = Math.ceil(x);
		this.y = Math.ceil(y);
		this.z = Math.ceil(z);
		return this;
	}

	public Vector3d round() {
		this.x = (int) x;
		this.y = (int) y;
		this.z = (int) z;
		return this;
	}

	public Vector3d clone() {
		return new Vector3d(this.x, this.y, this.z);
	}

	public String toString() {
		return x + " " + y + " " + z;
	}

	public static Vector3d fromPosition(@NonNull Position p) {
		return new Vector3d(p.getX(), p.getY(), p.getZ());
	}

	public static Position toPosition(@NonNull Vector3d d) {
		return new Position((int) Math.floor(d.x), (int) Math.floor(d.y), (int) Math.floor(d.z));
	}

	public static Vector3d fromString(@NonNull String s) {
		// String format: "X Y Z"
		try {
			double x = Double.parseDouble(s.split(" ")[0]);
			double y = Double.parseDouble(s.split(" ")[1]);
			double z = Double.parseDouble(s.split(" ")[2]);
			return new Vector3d(x, y, z);
		} catch (Exception e) {
			throw new IllegalArgumentException(s);
		}
	}

}
