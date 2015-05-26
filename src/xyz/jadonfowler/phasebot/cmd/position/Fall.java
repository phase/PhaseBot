package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.Session;

import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.block.Block;
import xyz.jadonfowler.phasebot.block.Material;
import xyz.jadonfowler.phasebot.cmd.Command;

public class Fall extends Command {

	boolean s;

	@Override
	public void exec(String in, String[] args, Session ses) {
		while (new Block(PhaseBot.getBot().pos.x, PhaseBot.getBot().pos.y - 2, PhaseBot.getBot().pos.z).getMaterial() != Material.AIR) {
			if (s) {
				PhaseBot.getBot().move(1, -1, 1);
				s = !s;
			} else {
				PhaseBot.getBot().move(-1, -1, -1);
				s = !s;
			}
		}
	}

	@Override
	public String getCommand() {
		return "fall";
	}

	@Override
	public String getDescription() {
		// TODO Auto-generated method stub
		return null;
	}

}
