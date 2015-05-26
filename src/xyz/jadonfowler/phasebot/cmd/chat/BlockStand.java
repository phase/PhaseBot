package xyz.jadonfowler.phasebot.cmd.chat;

import org.spacehq.packetlib.Session;

import xyz.jadonfowler.phasebot.PhaseBot;
import xyz.jadonfowler.phasebot.block.Block;
import xyz.jadonfowler.phasebot.cmd.Command;

public class BlockStand extends Command {

	@Override
	public void exec(String in, String[] args, Session s) {
		System.out.println("B: " + PhaseBot.getBot().pos.x + " " + 
				(PhaseBot.getBot().pos.y-2) + " " + 
				PhaseBot.getBot().pos.z);
		PhaseBot.getBot()
		.say("I am standing on: "
						+ new Block(
								PhaseBot.getBot().pos.x,
								PhaseBot.getBot().pos.y-2,
								PhaseBot.getBot().pos.z).
								getMaterial().toString());
	}

	@Override
	public String getCommand() {
		return "block";
	}

	@Override
	public String getDescription() {
		return null;
	}

}
