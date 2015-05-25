package xyz.jadonfowler.phasebot.cmd;

import org.spacehq.mc.protocol.data.game.values.entity.player.GameMode;
import org.spacehq.mc.protocol.data.game.values.setting.Difficulty;
import org.spacehq.mc.protocol.data.game.values.world.WorldType;
import org.spacehq.mc.protocol.packet.ingame.server.ServerRespawnPacket;
import org.spacehq.packetlib.Session;

public class Respawn extends Command {

	@Override
	public void exec(String in, String[] args, Session s) {
		s.send(new ServerRespawnPacket(0, Difficulty.NORMAL, GameMode.SURVIVAL, WorldType.DEFAULT));
	}

	@Override
	public String getCommand() {
		return "respawn";
	}

	@Override
	public String getDescription() {
		return "respawn";
	}

}
