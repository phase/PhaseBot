package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.Session;

import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;

public class Dig extends Command {

	@Override
	public void exec(String in, String[] args, final Session s) {
		try {
			int x = Integer.parseInt(args[1]);
			int y = Integer.parseInt(args[2]);
			int z = Integer.parseInt(args[3]);
			PhaseBot.getBot().breakBlock(x, y, z);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getCommand() {
		return "dig";
	}

	@Override
	public String getDescription() {
		return "Dig a block";
	}

}
