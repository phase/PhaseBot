package xyz.jadonfowler.phasebot.entity;

import java.util.ArrayList;
import java.util.UUID;

public class Player extends Entity {

	public static ArrayList<Player> players = new ArrayList<Player>();

	private UUID uuid;

	public Player(int i, UUID uuid, double x, double y, double z) {
		this(i, uuid, x, y, z, 0, 0);
	}

	public Player(int i, UUID uuid, double x, double y, double z, float yaw, float pitch) {
		super(i, "PLAYER", x, y, z);
	}

	public UUID getUUID() {
		return uuid;
	}

	public String getName() {
		return "";
	}

	public static Player byUUID(UUID u) {
		for (Player p : players)
			if (p.getUUID().equals(u))
				return p;
		return null;
	}

}
