package xyz.jadonfowler.phasebot.util;

import lombok.*;
import org.spacehq.mc.protocol.data.game.*;
import xyz.jadonfowler.phasebot.world.*;

public class Vector3d {
    @Getter public double x;
    @Getter public double y;
    @Getter public double z;

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

    public Vector3d negate() {
        this.x = -x;
        this.y = -y;
        this.z = -z;
        return this;
    }

    public Vector3d add(Vector3d v) {
        this.x += v.x;
        this.y += v.y;
        this.z += v.z;
        return this;
    }

    public int getBlockX() {
        return (int) this.clone().floor().round().x;
    }

    public int getBlockY() {
        return (int) this.clone().floor().round().y;
    }

    public int getBlockZ() {
        return (int) this.clone().floor().round().z;
    }

    public Vector3d setX(double x) {
        this.x = x;
        return this;
    }

    public Vector3d setY(double y) {
        this.y = y;
        return this;
    }

    public Vector3d setZ(double z) {
        this.z = z;
        return this;
    }

    public Vector3d addX(double x) {
        this.x += x;
        return this;
    }

    public Vector3d addY(double y) {
        this.y += y;
        return this;
    }

    public Vector3d addZ(double z) {
        this.z += z;
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
        }
        catch (Exception e) {
            throw new IllegalArgumentException(s);
        }
    }

    public Block getBlock() {
        return Block.getBlock(floor().round());
    }

    public Position toPosition() {
        return toPosition(this);
    }
}
