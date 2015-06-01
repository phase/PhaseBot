package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.mc.protocol.data.game.values.*;
import org.spacehq.packetlib.*;
import xyz.jadonfowler.phasebot.*;
import xyz.jadonfowler.phasebot.cmd.*;
import xyz.jadonfowler.phasebot.util.*;

public class Place extends Command {

	@Override
	public void exec(String in, String[] args, Session s) {
		try {
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			int z = Integer.parseInt(args[3]);
			Vector3d v = PhaseBot.getBot().relativeToAbsolute(new Vector3d(x, y, z));
			Face f = PhaseBot.getBot().getPlaceFace(v);
			PhaseBot.getBot().say(
					"I will place a block at " + v.x + " " + v.y + " " + v.z + " on face "
							+ f + ".");
			//PhaseBot.getBot().client.getSession().send(new ClientPlayerPlaceBlockPacket(Vector3d.toPosition(v), f));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCommand() {
		return "place";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
