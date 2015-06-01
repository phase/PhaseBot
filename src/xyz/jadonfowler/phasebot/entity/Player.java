package xyz.jadonfowler.phasebot.entity;

import java.util.*;
import lombok.*;
import xyz.jadonfowler.phasebot.util.*;

public class Player extends Entity {

	public static ArrayList<Player> players = new ArrayList<Player>();

	@Getter private UUID uuid;
	@Getter private String name;
	
	public Player(int i, UUID uuid, double x, double y, double z) {
		this(i, uuid, x, y, z, 0, 0);
	}

	public Player(int i, UUID uuid, double x, double y, double z, float yaw, float pitch) {
		super(i, "PLAYER", x, y, z);
		this.uuid = uuid;
		NameFetcher nf = new NameFetcher(Arrays.asList(uuid));
		try {
			name = nf.call().get(uuid);
		} catch (Exception e) {
			name = "NULL";
		}
		players.add(this);
	}

	public static Player byUUID(UUID u) {
		for (Player p : players)
			if (p.uuid.equals(u))
				return p;
		return null;
	}

}
