package xyz.jadonfowler.phasebot.cmd.position;

import org.spacehq.packetlib.Session;

import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.cmd.Command;

public class Move extends Command {

	@Override
	public void exec(String in, String[] args, Session s) {
		double rx = Double.parseDouble(args[1]);
		double ry = Double.parseDouble(args[2]);
		double rz = Double.parseDouble(args[3]);
		System.out.println("Move: " + (PhaseBot.getBot().pos.x + rx) + " " + (PhaseBot.getBot().pos.y + ry) + " "
				+ (PhaseBot.getBot().pos.z + rz));
		PhaseBot.getBot().move(rx, ry, rz);
	}

	@Override
	public String getCommand() {
		return "move";
	}

	@Override
	public String getDescription() {
		return "Move the bot to a position.";
	}

}
